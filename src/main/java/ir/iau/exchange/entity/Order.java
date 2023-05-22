package ir.iau.exchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User requester;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Asset source;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Asset destination;

    @Column(nullable = false)
    private BigDecimal completed = BigDecimal.ZERO;

    @Column(unique = true)
    private UUID trackingCode;

    @Column(nullable = false)
    private Boolean isClosed = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date submitDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date closeDate;

}
