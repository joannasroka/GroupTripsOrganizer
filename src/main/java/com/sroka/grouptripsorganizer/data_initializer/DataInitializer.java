package com.sroka.grouptripsorganizer.data_initializer;

import com.github.javafaker.Faker;
import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillCategory;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.bill.Currency;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus.ACTIVE;
import static com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus.REGISTERED;
import static com.sroka.grouptripsorganizer.entity.bill.BillCategory.BILL_CATEGORIES;
import static com.sroka.grouptripsorganizer.entity.bill.Currency.CURRENCIES;
import static com.sroka.grouptripsorganizer.entity.bill.SplitCategory.EQUALLY;
import static java.math.RoundingMode.UP;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private static final String PASSWORD = "Test123!";

    private static final int GROUPS_AMOUNT = 3;
    private static final int PARTICIPANTS_AMOUNT = 2;

    private static final int ACTIVE_USER_AMOUNT = 9;
    private static final int REGISTERED_USER_AMOUNT = 3;

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final BillShareRepository billShareRepository;

    private final PasswordEncoder passwordEncoder;

    private Faker faker = new Faker();

    @Transactional
    @Override
    public void run(String... args) {
        initializeData();
    }

    private void initializeData() {
        initializeUsers();
        initializeTrips();
        initializeParticipants();
        initializeBills();
        log.info("Data initialized.");
    }

    private void initializeUsers() {
        List<User> activeUsers = new ArrayList<>();
        List<User> registeredUsers = new ArrayList<>();

        IntStream.range(0, ACTIVE_USER_AMOUNT).forEach(i -> activeUsers.add(createUser(i, ACTIVE)));
        IntStream.range(0, REGISTERED_USER_AMOUNT).forEach(i -> registeredUsers.add(createUser(i, REGISTERED)));

        userRepository.saveAll(Stream.of(activeUsers, registeredUsers).flatMap(Collection::stream).collect(Collectors.toList()));
    }

    private User createUser(int number, AccountStatus accountStatus) {
        String password = null;

        if (ACTIVE.equals(accountStatus)) {
            password = passwordEncoder.encode(PASSWORD);
        }

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        if (number == 0 && accountStatus == ACTIVE) {
            firstName = "John";
            lastName = "Smith";
        }

        String mail = firstName.toLowerCase(Locale.ROOT) + "." + lastName.toLowerCase(Locale.ROOT) + "@mail.com";
        String phoneNumber = faker.numerify("#########");
        LocalDate dateOfBirth = generateRandomDateInPeriodOfTime(LocalDate.of(1960, 1, 1), LocalDate.of(2010, 1, 1));

        User user = new User(mail, password, firstName, lastName, phoneNumber, dateOfBirth);
        user.setAccountStatus(accountStatus);

        return user;
    }

    private void initializeTrips() {
        List<Trip> trips = new ArrayList<>();

        IntStream.range(0, GROUPS_AMOUNT).forEach(i -> trips.add(createTrip(i)));
        tripRepository.saveAll(Stream.of(trips).flatMap(Collection::stream).collect(Collectors.toList()));
    }


    private Trip createTrip(int number) {
        String name = faker.address().country() + " " + faker.number().numberBetween(2018, 2021);

        if (name.length() >= 30) {
            name = name.substring(0, 30);
        }

        byte[] picture = new byte[]{};

        List<User> users = userRepository.findAllByAccountStatus(ACTIVE);

        return new Trip(name, users.get(number % users.size()), picture);
    }

    private void initializeParticipants() {
        tripRepository.findAll().forEach(this::addParticipants);
    }

    private void addParticipants(Trip trip) {
        List<User> users = userRepository.findAll();
        users.removeAll(trip.getParticipants());

        Random rand = new Random();
        for (int i = 0; i < PARTICIPANTS_AMOUNT; i++) {
            if (users.isEmpty()) {
                return;
            }
            int randomUserNumber = rand.nextInt(users.size());
            User newParticipant = users.get(randomUserNumber);
            trip.addParticipant(newParticipant);
            log.info("Adding user: " + newParticipant + " to group: " + trip);
            users.remove(newParticipant);
        }
    }

    private LocalDate generateRandomDateInPeriodOfTime(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return startDate.plusDays(new Random().nextInt((int) days + 1));
    }

    private void initializeBills() {
        tripRepository.findAll().forEach(this::addBill);
    }

    private void addBill(Trip trip) {
        Set<User> users = trip.getParticipants();
        User owner = trip.getOwner();

        BigDecimal billTotalAmount = getRandomBillTotalAmount();
        String billName = faker.commerce().productName();

        if (billName.length() >= 100) {
            billName = billName.substring(0, 100);
        }

        BillCategory billCategory = getRandomBillCategory();
        Currency billCurrency = getRandomBillCurrency();
        LocalDate billDate = generateRandomDateInPeriodOfTime(LocalDate.of(2021, 1, 1), LocalDate.now());
        BigDecimal billShareAmount = billTotalAmount.divide(new BigDecimal(users.size()), 2, UP);

        Bill newBill = new Bill(billName, billCategory, owner, billDate, billTotalAmount,
                billCurrency, EQUALLY, trip, new ArrayList<>(), false);
        Bill savedBill = billRepository.save(newBill);

        users.forEach(user -> {
            BillShare billShare = new BillShare(owner, user, billShareAmount, savedBill);
            if (owner.equals(user)) {
                billShare.setPaid(true);
            }
            billShareRepository.save(billShare);
        });
    }

    private BigDecimal getRandomBillTotalAmount() {
        BigInteger bigInteger = BigInteger.probablePrime(10, new Random());
        return new BigDecimal(bigInteger);
    }

    private BillCategory getRandomBillCategory() {
        int size = BILL_CATEGORIES.size();
        Random random = new Random();

        return BILL_CATEGORIES.get(random.nextInt(size));
    }

    private Currency getRandomBillCurrency() {
        int size = CURRENCIES.size();
        Random random = new Random();

        return CURRENCIES.get(random.nextInt(size));
    }
}
