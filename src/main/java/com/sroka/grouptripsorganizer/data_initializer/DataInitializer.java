package com.sroka.grouptripsorganizer.data_initializer;

import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillCategory;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.bill.Currency;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.group.GroupRepository;
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

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final BillShareRepository billShareRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        initializeData();
    }

    private void initializeData() {
        initializeUsers();
        initializeGroups();
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
        String mail = "user" + number + "." + accountStatus.toString().toLowerCase() + "@mail.com";
        String password = null;

        if (ACTIVE.equals(accountStatus)) {
            password = passwordEncoder.encode(PASSWORD);
        }

        String firstName = "FirstName" + number + "." + accountStatus.toString().toLowerCase();
        String lastName = "LastName" + number + "." + accountStatus.toString().toLowerCase();
        String phoneNumber = "123456789";
        LocalDate dateOfBirth = generateRandomDateInPeriodOfTime(LocalDate.of(1960, 1, 1), LocalDate.of(2010, 1, 1));

        User user = new User(mail, password, firstName, lastName, phoneNumber, dateOfBirth);
        user.setAccountStatus(accountStatus);

        return user;
    }

    private void initializeGroups() {
        List<Group> groups = new ArrayList<>();

        IntStream.range(0, GROUPS_AMOUNT).forEach(i -> groups.add(createGroup(i)));
        groupRepository.saveAll(Stream.of(groups).flatMap(Collection::stream).collect(Collectors.toList()));
    }


    private Group createGroup(int number) {
        String name = "Group" + number;

        List<User> users = userRepository.findAllByAccountStatus(ACTIVE);

        return new Group(name, users.get(number % users.size()));
    }

    private void initializeParticipants() {
        groupRepository.findAll().forEach(this::addParticipants);
    }

    private void addParticipants(Group group) {
        List<User> users = userRepository.findAll();
        users.removeAll(group.getParticipants());

        Random rand = new Random();
        for (int i = 0; i < PARTICIPANTS_AMOUNT; i++) {
            if (users.isEmpty()) {
                return;
            }
            int randomUserNumber = rand.nextInt(users.size());
            User newParticipant = users.get(randomUserNumber);
            group.addParticipant(newParticipant);
            log.info("Adding user: " + newParticipant + " to group: " + group);
            users.remove(newParticipant);
        }
    }

    private LocalDate generateRandomDateInPeriodOfTime(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return startDate.plusDays(new Random().nextInt((int) days + 1));
    }

    private void initializeBills() {
        groupRepository.findAll().forEach(this::addBill);
    }

    private void addBill(Group group) {
        Set<User> users = group.getParticipants();
        User owner = group.getOwner();

        BigDecimal billTotalAmount = getRandomBillTotalAmount();
        String billName = "Bill" + owner.getId();
        BillCategory billCategory = getRandomBillCategory();
        Currency billCurrency = getRandomBillCurrency();
        LocalDate billDate = generateRandomDateInPeriodOfTime(LocalDate.of(2021, 1, 1), LocalDate.now());
        BigDecimal billShareAmount = billTotalAmount.divide(new BigDecimal(users.size()), 2, UP);

        Bill newBill = new Bill(billName, billCategory, owner, billDate, billTotalAmount,
                billCurrency, EQUALLY, group, new ArrayList<>(), false);
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
