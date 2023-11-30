package com.sqlite.entity;

public class UserEntity {

		private int id;
		private String firstName;
		private int age;
		
		public UserEntity() {
		}
		
		public UserEntity(String firstName, int age) {
			this.firstName = firstName;
			this.age = age;
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		@Override
		public String toString() {
			return "UserEntity [id=" + id + ", firstName=" + firstName + ", age=" + age + "]";
		}
	}
