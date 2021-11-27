package com.sroka.grouptripsorganizer.service.bill;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.dto.bill.BillUpdateDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.bill.Currency;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.BillIsNotSettledException;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.exception.InvalidExchangeRateException;
import com.sroka.grouptripsorganizer.mapper.BillMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import com.sroka.grouptripsorganizer.utils.LocalDateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import static com.sroka.grouptripsorganizer.entity.bill.Currency.PLN;
import static java.math.RoundingMode.HALF_UP;
import static java.math.RoundingMode.UP;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BillService {
    private final String EXCHANGE_RATES_PATH = "http://api.nbp.pl/api/exchangerates/rates/a/%s/%s/";

    private final BillMapper billMapper;

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final BillShareRepository billShareRepository;
    private final ObjectMapper objectMapper;

    public BillDto create(BillCreateDto billCreateDto, Long executorId) {
        Trip trip = tripRepository.getById(billCreateDto.getTripId());
        User executor = userRepository.getById(executorId);
        User payer = userRepository.getById(billCreateDto.getPayerId());

        validate(executor, trip);
        validate(payer, trip);

        Bill newBill = billMapper.convertToEntity(billCreateDto);
        newBill.setTrip(trip);
        newBill.setPayer(payer);
        newBill.setPaid(false);
        convertAmount(newBill, billCreateDto.getOriginalCurrency(), billCreateDto.getSelectedCurrency());

        Bill savedBill = billRepository.save(newBill);

        return billMapper.convertToDto(savedBill);
    }

    public BillDto update(BillUpdateDto billUpdateDto, Long billId, Long executorId) {
        Bill bill = billRepository.getById(billId);

        Trip trip = bill.getTrip();
        User executor = userRepository.getById(executorId);

        validate(executor, trip);

        bill.setCategory(billUpdateDto.getCategory());
        bill.setDate(billUpdateDto.getDate());
        bill.setTitle(billUpdateDto.getTitle());

        Currency originalCurrency = bill.getCurrency();
        Currency selectedCurrency = billUpdateDto.getSelectedCurrency();

        if (selectedCurrency != null && !originalCurrency.equals(selectedCurrency)) {
            convertAmount(bill, bill.getCurrency(), billUpdateDto.getSelectedCurrency());
            updateAllBillShares(bill, originalCurrency, selectedCurrency);
        }

        return billMapper.convertToDto(bill);
    }


    public void delete(Long billId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Bill billToDelete = billRepository.getById(billId);
        Trip trip = billToDelete.getTrip();

        validate(executor, trip);
        validateBillToDelete(billToDelete);

        billRepository.delete(billToDelete);
    }

    @Transactional(readOnly = true)
    public Page<BillDto> getByTrip(Long tripId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Trip trip = tripRepository.getById(tripId);
        validate(executor, trip);

        Page<Bill> bills = billRepository.findAllByTripOrderByDate(trip, pageable);
        return bills.map(billMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public BillDto getById(Long billId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Bill bill = billRepository.getById(billId);
        Trip trip = bill.getTrip();

        validate(executor, trip);

        return billMapper.convertToDto(bill);
    }

    private void convertAmount(Bill bill, Currency originalCurrency, Currency selectedCurrency) {
        if (selectedCurrency == null || originalCurrency.equals(selectedCurrency)) {
            bill.setCurrency(originalCurrency);
            return;
        }

        BigDecimal totalAmount = bill.getTotalAmount();
        if (originalCurrency != PLN) {
            String pathToPln = formatToCorrectExchangeRatesPath(bill.getDate(), originalCurrency);
            BigDecimal multiplierToPln = getExchangeRate(pathToPln, originalCurrency);
            totalAmount = totalAmount.multiply(multiplierToPln).setScale(2, HALF_UP);
        }

        if (selectedCurrency != PLN) {
            String pathToSelected = formatToCorrectExchangeRatesPath(bill.getDate(), selectedCurrency);
            BigDecimal multiplierToSelected = getExchangeRate(pathToSelected, selectedCurrency);
            totalAmount = totalAmount.divide(multiplierToSelected, 2, HALF_UP);
        }

        bill.setCurrency(selectedCurrency);
        bill.setTotalAmount(totalAmount.setScale(2));
    }

    private void convertAmount(Bill bill, BillShare billShare, Currency originalCurrency, Currency selectedCurrency) {
        if (selectedCurrency == null || originalCurrency.equals(selectedCurrency)) {
            return;
        }
        LocalDate exchangeRateDate = bill.getDate();

        BigDecimal totalAmount = billShare.getAmount();
        if (originalCurrency != PLN) {
            String pathToPln = formatToCorrectExchangeRatesPath(exchangeRateDate, originalCurrency);
            BigDecimal multiplierToPln = getExchangeRate(pathToPln, originalCurrency);
            totalAmount = totalAmount.multiply(multiplierToPln).setScale(2, HALF_UP);
        }

        if (selectedCurrency != PLN) {
            String pathToSelected = formatToCorrectExchangeRatesPath(exchangeRateDate, selectedCurrency);
            BigDecimal multiplierToSelected = getExchangeRate(pathToSelected, selectedCurrency);
            totalAmount = totalAmount.divide(multiplierToSelected, 2, HALF_UP);
        }

        billShare.setAmount(totalAmount.setScale(2));
    }


    private BigDecimal getExchangeRate(String path, Currency currency) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();

            if (code == HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        result.append(line);
                    }
                }
                JsonNode foo = objectMapper.readTree(result.toString());
                String rateToString = foo.findValue("mid").asText();
                BigDecimal rate = new BigDecimal(rateToString).setScale(2, UP);
                return rate;
            } else if (code == HTTP_NOT_FOUND) {
                result = new StringBuilder();
                path = formatToCorrectExchangeRatesPath(currency);
                url = new URL(path);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        result.append(line);
                    }
                }
                JsonNode foo = objectMapper.readTree(result.toString());
                String rateToString = foo.findValue("mid").asText();
                BigDecimal rate = new BigDecimal(rateToString).setScale(2, UP);
                return rate;
            }
            throw new InvalidExchangeRateException();
        } catch (Exception e) {
            throw new InvalidExchangeRateException();
        }
    }

    private String formatToCorrectExchangeRatesPath(LocalDate date, Currency currency) {
        String billDate = LocalDateTimeFormatter.format(date);
        return EXCHANGE_RATES_PATH.formatted(currency.toString(), billDate);
    }

    private String formatToCorrectExchangeRatesPath(Currency currency) {
        return EXCHANGE_RATES_PATH.formatted(currency.toString(), "last");
    }

    private void updateAllBillShares(Bill bill, Currency originalCurrency, Currency selectedCurrency) {
        List<BillShare> billShareList = billShareRepository.getByBillId(bill.getId());
        billShareList.forEach(billShare -> convertAmount(bill, billShare, originalCurrency, selectedCurrency));
    }

    private void validate(User user, Trip trip) {
        if (!trip.getParticipants().contains(user)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateBillToDelete(Bill bill) {
        if (!bill.isPaid()) {
            throw new BillIsNotSettledException();
        }
    }
}
