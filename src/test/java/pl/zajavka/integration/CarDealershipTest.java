package pl.zajavka.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import pl.zajavka.business.*;
import pl.zajavka.business.dao.CarDAO;
import pl.zajavka.business.dao.CustomerDAO;
import pl.zajavka.business.dao.SalesmanDAO;
import pl.zajavka.infrastructure.configuration.HibernateUtil;
import pl.zajavka.infrastructure.database.repository.CarDealershipManagementRepository;
import pl.zajavka.infrastructure.database.repository.CarRepository;
import pl.zajavka.infrastructure.database.repository.CustomerRepository;
import pl.zajavka.infrastructure.database.repository.SalesmanRepository;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarDealershipTest {

    private CarDealershipManagementService carDealershipManagementService;
    private CarPurchaseService carPurchaseService;
    private CarServiceRequestService carserviceRequestService;
    private CarServiceProcessingService carServiceProcessingService;

    @BeforeEach
    void beforeEach() {

        CarDAO carDAO = new CarRepository();
        SalesmanDAO salesmanDAO = new SalesmanRepository();
        CustomerDAO customerDAO = new CustomerRepository();
        FileDataPreparationService fileDataPreparationService = new FileDataPreparationService();
        CustomerService customerService = new CustomerService(customerDAO);
        CarService carService = new CarService(carDAO);
        SalesmanService salesmanService = new SalesmanService(salesmanDAO);

        this.carDealershipManagementService = new CarDealershipManagementService(
                new CarDealershipManagementRepository(),
                fileDataPreparationService
        );

        this.carPurchaseService = new CarPurchaseService(
                fileDataPreparationService,
                customerService,
                carService,
                salesmanService
        );

        this.carserviceRequestService = new CarServiceRequestService(
                fileDataPreparationService,
                carService,
                customerService
        );

        this.carServiceProcessingService = new CarServiceProcessingService(
            fileDataPreparationService
        );
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.closeSessionFactory();
    }

    @Test
    @Order(1)
    void purge() {
        log.info("### RUNNING ORDER 1");
        carDealershipManagementService.purge();
    }

    @Test
    @Order(2)
    void init() {
        log.info("### RUNNING ORDER 2");
        carDealershipManagementService.init();
    }

    @Test
    @Order(3)
    void purchase() {
        log.info("### RUNNING ORDER 3");
        carPurchaseService.purchase();
    }

    @Test
    @Order(4)
    void processServiceRequest() {
        log.info("### RUNNING ORDER 4");
        carserviceRequestService.requestService();
    }

    @Test
    @Order(5)
    void printCarHistory() {
        log.info("### RUNNING ORDER 5");
        carServiceProcessingService.process();
    }
}
