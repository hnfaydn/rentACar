package com.turkcell.rentACar.business.constants.messages;

public class BusinessMessages {

        public static final String TO = " to ";

    public class GlobalMessages{

        public static final String DATA_LISTED_SUCCESSFULLY = "Data Listed Successfully: ";
        public static final String DATA_ADDED_SUCCESSFULLY = "Data Added Successfully: ";
        public static final String DATA_BROUGHT_SUCCESSFULLY = "Data Brought Successfully: ";
        public static final String DATA_UPDATED_TO_NEW_DATA = "Data updated to new data: ";
        public static final String DATA_DELETED_SUCCESSFULLY = "Data deleted successfully: ";
    }

    public class AdditionalServiceMessages{

        public static final String ADDITIONAL_SERVICE_ALREADY_EXISTS = "Following Additional Service is already exists: ";
        public static final String ADDITIONAL_SERVICE_NOT_FOUND = "There is no Additional Service with following id: ";
        public static final String ADDITIONAL_SERVICE_ORDERED_BY_RENTAL_CARS = "This Additional Service is Ordered by following rental car Ids: ";
    }

    public class BrandMessages{

        public static final String BRAND_ALREADY_EXISTS = "Following Brand is already exists: ";
        public static final String BRAND_NOT_FOUND = "There is no Brand with following id: ";
    }

    public class CarDamageMessages{

        public static final String CAR_DAMAGE_ALREADY_EXISTS = "Following Car Damage is already exists: ";
        public static final String CAR_DAMAGE_NOT_FOUND = "There is no Car Damage with following id: ";
    }

    public class CarMaintenanceMessages{

        public static final String CAR_NOT_FOUND = "There is no Car with following id: ";
        public static final String CAR_RENT_NOT_FOUND = "There is no Car Rent operation with following id: ";
        public static final String CAR_IS_UNDER_MAINTENANCE_UNKNOWN_RETURN_DATE = "Car is under maintenance and return date is unknown!";
        public static final String CAR_IS_RENTED = "Maintenance is not possible! This car is rented from ";
        public static final String CAR_MAINTENANCE_NOT_FOUND = "There is no car maintenance with following id: ";
        public static final String NO_CHANGES_NO_NEED_TO_UPDATE = "Initial values are completely equal to update values, no need to update!";
        public static final String CAR_UNDER_MAINTENANCE_UNTIL = "This car is under maintenance until: ";
        public static final String RETURN_DATE_CANNOT_BEFORE_CURRENT_DAY = "Return date cannot before current day!";
        public static final String CAR_RENTED_RETURN_UNKNOWN = "Car is rented and return date is not estimated.";
        public static final String CAR_IS_UNDER_MAINTENANCE_RETURN_DATE_KNOWN = "Maintenance update is not possible! This car is rented from ";
    }

    public class CarMessages{

        public static final String CAR_ALREADY_EXISTS = "This Car is already exists!";
        public static final String CAR_NOT_FOUND = "There is no Car with following id: ";
        public static final String NO_CHANGES_NO_NEED_TO_UPDATE = "Initial values are completely equal to update values, no need to update!";
        public static final String BRAND_NOT_FOUND = "There is no Brand with following id: ";
        public static final String COLOR_NOT_FOUND = "There is no Color with following id: ";
        public static final String PAGE_NO_CANNOT_LESS_THAN_ZERO = "Page No can not less than or equal to zero";
        public static final String PAGE_SIZE_CANNOT_LESS_THAN_ZERO = "Page Size can not less than or equal to zero";
        public static final String DAILY_PRICE_CANNOT_LESS_THAN_ZERO = "Daily price can not less than or equal to zero";
        public static final String CAR_DAMAGE_NOT_FOUND = "There is no Car Damage with following id: ";
    }

    public class CityMessages{

        public static final String CITY_NAME_ALREADY_EXISTS = "Following City Name is already exists: ";
        public static final String CITY_ID_ALREADY_EXISTS = "Following City Id is already exists: ";
        public static final String CITY_NOT_FOUND = "There is no City with following id: ";
    }

    public class ColorMessages{

        public static final String COLOR_ALREADY_EXISTS = "Following Color is already exists: ";
        public static final String COLOR_NOT_FOUND = "There is no Color with following id: ";
    }

    public class CorporateCustomerMessages{

        public static final String CORPORATE_CUSTOMER_NOT_FOUND = "There is no Corporate Customer with following id: ";
        public static final String CORPORATE_CUSTOMER_EMAIL_ALREADY_EXISTS = "Following Email is already exists: ";
        public static final String CORPORATE_CUSTOMER_TAX_NUMBER_ALREADY_EXISTS = "Following Tax Number is already exists: ";
    }

    public class CustomerMessages{

        public static final String CUSTOMER_NOT_FOUND = "There is no Customer with following id: ";
        public static final String CUSTOMER_EMAIL_ALREADY_EXISTS = "Following Email is already exists: ";
    }

    public class IndividualCustomerMessages{

        public static final String INDIVIDUAL_CUSTOMER_NOT_FOUND = "There is no Individual Customer with following id: ";
        public static final String INDIVIDUAL_CUSTOMER_EMAIL_ALREADY_EXISTS = "Following Email is already exists: ";
        public static final String INDIVIDUAL_CUSTOMER_NATIONAL_IDENTITY_ALREADY_EXISTS = "Following National Identity is already exists: ";
    }

    public class InvoiceMessages{

        public static final String RENTAL_CAR_NOT_FOUND = "There is no Rental Car with following id: ";
        public static final String INVOICE_NOT_FOUND = "There is no Invoice with following id: ";
    }

    public class RentalCarMessages{

        public static final String ADDITIONAL_SERVICE_NOT_FOUND = "There is no Additional Service with following id: ";
        public static final String CITY_NOT_FOUND = "There is no City with following id: ";
        public static final String RENT_AND_RETURN_KILOMETER_NOT_VALID = "Return kilometer can not before Rent kilometer.";
        public static final String CAR_MAINTENANCE_NOT_FOUND = "There is no car maintenance with following id: ";
        public static final String CAR_IS_UNDER_MAINTENANCE_UNKNOWN_RETURN_DATE = "Car is under maintenance and return date is unknown!";
        public static final String CAR_IS_UNDER_MAINTENANCE_UNTIL = "This car is under maintenance until: ";
        public static final String CAR_NOT_FOUND = "There is no Car with following id: ";
        public static final String RETURN_DATE_CANNOT_BEFORE_RENT_DAY = "Return date cannot before rent day!";
        public static final String RENTAL_CAR_NOT_FOUND = "There is no Rental Car with following id: ";
        public static final String NO_CHANGES_NO_NEED_TO_UPDATE = "Initial values are completely equal to update values, no need to update!";
        public static final String CAR_IS_RENTED_UNKNOWN_RETURN_DATE = "Car is Rented and return date is unknown!";
        public static final String CAR_IS_RENTED_RETURN_DATE_KNOWN = "This car Rented from ";
        public static final String CAR_UNDER_MAINTENANCE_RENT_NOT_POSSIBLE_UNTIL = "Rental update is not possible! This car is under maintenance until: ";
        public static final String CUSTOMER_NOT_FOUND = "There is no Customer with following id: ";
        public static final String RETURN_KILOMETER_NOT_VALID_FOR_CAR = "Return kilometer is not valid for following car id: ";

    }

    public class UserMessages{

        public static final String USER_NOT_FOUND = "There is no Customer with following id: ";
        public static final String USER_EMAIL_ALREADY_EXISTS = "Following Email is already exists: ";
    }

    public class PaymentMessages{

        public static final String INVALID_PAYMENT = "Invalid payment operation!";

    }

}
