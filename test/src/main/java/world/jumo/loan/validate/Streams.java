/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Streams.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 */
public class Streams {

    private static final Logger LOG = Logger.getLogger(Streams.class.getName());

    /**
     *
     */
    static class SumCount {

        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal count = BigDecimal.ZERO;

        SumCount() {

            super();
        }

        SumCount(BigDecimal a) {

            sum = a;
            count = count.add(BigDecimal.ONE);
        }

        SumCount add(SumCount b) {

            this.sum = this.sum.add(b.sum);
            this.count = this.count.add(b.count);
            return this;
        }
    }

    /**
     * 
     */
    public Streams() {

        super();
    }

    public static void main(String[] args) {

        final Path out = Paths.get(args.length > 1 ? args[1] : "./Output.csv");
        final Path in = Paths.get(args.length > 0 ? args[0] : "./Loans.csv");
        try {
            Files.write(out,
                Files.readAllLines(in, StandardCharsets.UTF_8)
                    .stream()
                    .skip(1)
                    .map(s -> s.split(","))
                    .map(s -> new Loan(s[0], s[1], LocalDate.parse(s[2], DateTimeFormatter.ofPattern("''dd-MMM-yyyy''")), s[3], new BigDecimal(s[4])))
                    .collect(groupingBy(Function.identity(),
                        () -> new TreeMap<>(
                            Comparator.<Loan, String> comparing(Loan::getNetwork).thenComparing(Loan::getProduct).thenComparing(l -> l.getDate().getMonth())),
                        Collectors.reducing(new SumCount(), t -> new SumCount(t.getAmount()), (a, b) -> b.add(a))))
                    .entrySet()
                    .stream()
                    .map(e -> String.format("%s,%s,'%.3s',%s,%s", e.getKey().getNetwork(), e.getKey().getProduct(),
                        e.getKey().getDate().getMonth(), e.getValue().sum, e.getValue().count))
                    .collect(Collectors.joining(System.lineSeparator(), "Network,Product,Month,Sum,Count" + System.lineSeparator(), ""))
                    .getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e, () -> String.format("Could not process file %s", args.length > 0 ? args[0] : "./Loans.scv"));
        }
    }
}
