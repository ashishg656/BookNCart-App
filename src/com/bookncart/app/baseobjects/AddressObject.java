package com.bookncart.app.baseobjects;

import java.util.List;

public class AddressObject {

	List<SingleAddressObject> addresses;

	public List<SingleAddressObject> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<SingleAddressObject> addresses) {
		this.addresses = addresses;
	}

	public class SingleAddressObject {
		String name;
		String address_line_1;
		String address_line_2;
		String city;
		String state;
		String pincode;
		String mobile_number;
		int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress_line_1() {
			return address_line_1;
		}

		public void setAddress_line_1(String address_line_1) {
			this.address_line_1 = address_line_1;
		}

		public String getAddress_line_2() {
			return address_line_2;
		}

		public void setAddress_line_2(String address_line_2) {
			this.address_line_2 = address_line_2;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPincode() {
			return pincode;
		}

		public void setPincode(String pincode) {
			this.pincode = pincode;
		}

		public String getMobile_number() {
			return mobile_number;
		}

		public void setMobile_number(String mobile_number) {
			this.mobile_number = mobile_number;
		}

	}

}
