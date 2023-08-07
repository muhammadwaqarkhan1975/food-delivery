package com.food.delivery.factory;

import com.food.delivery.domain.Address;
import com.food.delivery.model.AddressModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressFactory
{

    /**
     * Returns an instance of address, purpose of this method is to insert for insertion persistent store
     *  Factory method for use by application code when constructing a new address for insertion into persistent store. A new
     *   @param street
     *                  This field contains the street information of address
     *   @param street2
     *                  This field contains the street2 information of address
     *   @param postalCode
     *                  This field contains the postal code information of address
     *   @param addressType
     *                  This field contains the address type like HOME,OFFICE, WORK
     *   @param country
     *                  This field contains the country
     *   @param latitude
     *                  This field contains the latitude of address to calculate the address
     *   @param longitude
     *                  This field contains the longitude of address to calculate the address
     * @return Address
     */
    public Address newAddress (String street, String street2, String postalCode, Address.AddressType addressType,
                               String city, String country, Double latitude, Double longitude)
    {
        return  new AddressBuilder()
                .withCity(city)
                .withCountry(country)
                .withPostalCode(postalCode)
                .withStreet(street)
                .withStreet2(street2)
                .withAddressType(addressType)
                .withLatitude(latitude)
                .withLongitude(longitude)
                .build();
    }

    /**
     * Factory method for use by application code when constructing a new address for insertion into persistent store. A new
     *
     * @param addressModel
     *            address model will contain the location information along with address details
     */
    public Address newAddress (AddressModel addressModel)
    {
      return  new AddressBuilder()
                .withCity(addressModel.city())
                .withCountry(addressModel.country())
                .withPostalCode(addressModel.postalCode())
                .withState(addressModel.state())
                .withStreet(addressModel.street())
                .withStreet2(addressModel.street2())
                .withAddressType(addressModel.addressType())
                .withCounty(addressModel.county())
                .withLatitude(addressModel.latitude())
                .withLongitude(addressModel.longitude())
                .build();
    }

    private static class AddressBuilder {
        private String city;
        private String country;
        private String postalCode;
        private String state;
        private String street;
        private String street2;
        private Address.AddressType addressType;
        private String county;
        private Double latitude;
        private Double longitude;

        private AddressBuilder() {
        }


        public AddressBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public AddressBuilder withPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public AddressBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public AddressBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder withStreet2(String street2) {
            this.street2 = street2;
            return this;
        }

        public AddressBuilder withAddressType(Address.AddressType addressType) {
            this.addressType = addressType;
            return this;
        }

        public AddressBuilder withCounty(String county) {
            this.county = county;
            return this;
        }

        public AddressBuilder withLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public AddressBuilder withLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        private Address build()
        {
            Address address = new Address();
            address.setCity(this.city);
            address.setCountry(this.country);
            address.setPostalCode(this.postalCode);
            address.setState(this.state);
            address.setStreet(this.street);
            address.setStreet2(this.street2);
            address.setAddressType(this.addressType);
            address.setCounty(this.county);
            address.setLatitude(this.latitude);
            address.setLongitude(this.longitude);

            return address;
        }

        public String toString() {
            return "AddressFactor.AddressFactorBuilder( city=" + this.city + ", country=" + this.country + ", postalCode=" + this.postalCode + ", state=" + this.state + ", street=" + this.street + ", street2=" + this.street2 + ", addressType=" + this.addressType + ", county=" + this.county + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ")";
        }
    }
}
