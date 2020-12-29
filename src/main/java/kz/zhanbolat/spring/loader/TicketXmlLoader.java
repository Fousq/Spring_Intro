package kz.zhanbolat.spring.loader;

import kz.zhanbolat.spring.entity.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Unmarshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class TicketXmlLoader implements Function<String, List<Ticket>> {
    private static final Logger logger = LoggerFactory.getLogger(TicketXmlLoader.class);
    private Unmarshaller unmarshaller;

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override
    public List<Ticket> apply(String filePath) {
        final Path path = new File(filePath).toPath();
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File not exists. file path: " + path.toAbsolutePath());
        }
        List<Ticket> tickets = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
            TicketContainer ticketContainer = (TicketContainer) unmarshaller.unmarshal(new StreamSource(fileInputStream));
            if (Objects.nonNull(ticketContainer.getTickets())) {
                tickets = ticketContainer.getTickets();
            }
        } catch (IOException e) {
            logger.error("Cannot unmarshal the tickets from file: " + path.toAbsolutePath(), e);
        }
        return tickets;
    }
}
