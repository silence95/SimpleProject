//package com.proto;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.pb.Person;
//
///**
// * @author siling
// * @Date 2018/1/3.
// */
//public class UseTestPb {
//
//  public static void main(String[] args) throws InvalidProtocolBufferException {
//    Person person = Person.newBuilder().setId(123).setName("A").build();
//    byte[] personBytes = person.toByteArray();
//    System.out.println("person:" + (new String(personBytes)));
//
//    Person personParsed = Person.parseFrom(personBytes);
//
//    System.out.println("personParsed:" + personParsed.getId() + " " + personParsed.getName());
//  }
//}
