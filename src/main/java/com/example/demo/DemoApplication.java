package com.example.demo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomRepository;
import com.monitorjbl.xlsx.StreamingReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class DemoApplication implements CommandLineRunner {

  private final CustomRepository customRepository;

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    log.info("Reading File...");
    long startTimeRead = System.currentTimeMillis();
    InputStream is = new FileInputStream("customber.xlsx");
    Workbook workbook = StreamingReader.builder()
        .rowCacheSize(50000)
        .bufferSize(4096)
        .open(is);

    List<Customer> customers = StreamSupport.stream(workbook.spliterator(), false)
        .flatMap(sheet -> StreamSupport.stream(sheet.spliterator(), false)
            .skip(1)
            .map(con -> {
              Customer customer = new Customer();
              customer.setId((long) con.getCell(0).getNumericCellValue());
              customer.setName(con.getCell(1).getStringCellValue());
              customer.setLastName(con.getCell(2).getStringCellValue());
              customer.setAddress(con.getCell(3).getStringCellValue());
              customer.setEmail(con.getCell(4).getStringCellValue());
              return customer;
            }))
        .collect(Collectors.toList());

    long endTimeRead = System.currentTimeMillis();
    log.info("-> READING FINISHED " + "-> TIME: " + (endTimeRead - startTimeRead) + "ms");

    long startWriter = System.currentTimeMillis();
    customRepository.saveAll(customers); // <- GUARDAR DATOS 
    long endWriter = System.currentTimeMillis();
    log.info("-> WRITING FINISHED " + "-> TIME: " + (endWriter - startWriter) + "ms");
  }

}
