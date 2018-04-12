/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Validator.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Validator {

    private static final Logger LOG = Logger.getLogger(Validator.class.getName());

    private ProductAggregator<Loan, NetworkProductMonthGroup, BigDecimal> aggregator;

    public Validator() {

        this.aggregator = new ProductAggregator<>(getConverter(), getAggregates());
    }

    private static Function<Loan, NetworkProductMonthGroup> getConverter() {

        return new Function<Loan, NetworkProductMonthGroup>() {

            @Override
            public NetworkProductMonthGroup apply(Loan t) {

                return new NetworkProductMonthGroup(t.getNetwork(), t.getProduct(), t.getDate().getMonth());
            }

        };
    }

    private static List<Class<? extends Aggregate<BigDecimal>>> getAggregates() {

        List<Class<? extends Aggregate<BigDecimal>>> list = new ArrayList<>();
        list.add(Sum.class);
        list.add(Count.class);
        return list;
    }

    private static Loan unmarshallLoan(String csv) {

        final String[] fields = csv.split(",");
        return new Loan(fields[0], fields[1], LocalDate.parse(fields[2], DateTimeFormatter.ofPattern("''dd-MMM-yyyy''")), fields[3], new BigDecimal(fields[4]));
    }

    private static String marshallResult(NetworkProductMonthGroup group, List<BigDecimal> agg) {

        final StringBuilder b = new StringBuilder();
        b.append(String.format("%s,%s,'%.3s'", group.getNetwork(), group.getProduct(), group.getMonth()));
        for (BigDecimal r : agg) {
            b.append(",").append(r);
        }
        return b.toString();
    }

    public void write(BufferedWriter writer) throws IOException {

        writer.write("Network,Product,Month,Sum,Count");
        writer.newLine();
        for (Map.Entry<NetworkProductMonthGroup, List<BigDecimal>> e : aggregator.get().entrySet()) {
            writer.write(Validator.marshallResult(e.getKey(), e.getValue()));
            writer.newLine();
        }
    }

    public void read(BufferedReader reader) throws IOException {

        // read the header
        String line = reader.readLine();
        line = reader.readLine();
        while (line != null) {
            aggregator.put(unmarshallLoan(line));
            line = reader.readLine();
        }
    }

    public static void main(String[] args) {

        final Path in = Paths.get(args.length > 0 ? args[0] : "./Loans.csv");
        final Path out = Paths.get(args.length > 1 ? args[1] : "./Output.csv");
        Validator v = new Validator();
        try (BufferedReader reader = Files.newBufferedReader(in, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            v.read(reader);
            v.write(writer);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e, () -> String.format("Failed to aggreate from file %s to %s", in, out));
        }
    }
}
