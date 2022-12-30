package com.elijah.onlinebankingapp.model.transaction;

import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.customer.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    private double amount;
    private double currentBalance;
    private String transactionType;
    private String description;
    private String depositorOrWithDrawalName;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount bankAccount;
}
//@Repository
//public interface TournamentRepository extends PagingAndSortingRepository<Tournament, Long> {
//    @Query("select * from tournament where start_date <=:date and end_date >=: dateCopy",nativeQuery=true)
//    List<Tournament> findByStartBeforeAndEndAfter(@Param("date")LocalDate date,@Param("dateCopy") LocalDate dateCopy); //today's date is passed as date and dateCopy
//
//}

//@Repository
//public interface TournamentRepository extends PagingAndSortingRepository<Tournament, Long> {
//    @Query("select t from Tournament t where t.startDate <=:date and t.endDate >=: dateCopy")
//    Page<Tournament> findByStartBeforeAndEndAfter(@Param("date")LocalDate date,@Param("dateCopy") LocalDate dateCopy, Pageable page); //today's date is passed as date and dateCopy
//
//}