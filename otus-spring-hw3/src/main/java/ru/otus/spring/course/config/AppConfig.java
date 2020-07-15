package ru.otus.spring.course.config;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.course.data.Line;
import ru.otus.spring.course.service.AppService;
import ru.otus.spring.course.service.ConsoleService;
import ru.otus.spring.course.service.LocaleTextService;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class AppConfig {

    @Value("${app.language}")
    private Locale language;

    @Value("${app.infinite}")
    private String isInfinite;

    @Bean
    public ConsoleService consoleService() {
        return new ConsoleService(new Scanner(System.in), System.out);
    }

    @Bean
    public LocaleTextService localeTextService(MessageSource messageSource, ConsoleService consoleService) {
        return new LocaleTextService(consoleService, language, messageSource);
    }

    @Bean
    public List<Line> lines(@Value("${csv.file}") String csvFile) throws Exception {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/" + csvFile + "_" + language + ".csv"), StandardCharsets.UTF_8);
        CSVReader csvReader = new CSVReader(reader);
        List<Line> list = new ArrayList<>();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            String question = nextRecord[0];
            int answerIndex = Integer.valueOf(nextRecord[1]);
            List<String> options = new ArrayList<>(Arrays.asList(nextRecord).subList(2, nextRecord.length));
            list.add(Line.builder()
                    .answer(answerIndex)
                    .options(options)
                    .question(question).build());
        }
        reader.close();
        csvReader.close();
        return list;
    }

    @Bean
    public AppService appService(LocaleTextService localeTextService, List<Line> lines) {
        return new AppService(localeTextService, lines, Boolean.parseBoolean(isInfinite));
    }
}
