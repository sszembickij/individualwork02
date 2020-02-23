import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Person {
    private String surname;
    private String dataFile;
    private int leftDaysInES = 90;
    private boolean validDates = true;
    private List<Stay> listStays = new ArrayList<>();

    void setValidDates(boolean validDates) {
        this.validDates = validDates;
    }

    {
        try (BufferedReader br = new BufferedReader(new FileReader("datesOfStay.txt"))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                listStays.add(new Stay(currentLine));
            }
        } catch (IOException e) {
            validDates = false;
            e.printStackTrace();
        }

    }

    public Person(String surname, String dataFile) {
        this.surname = surname;
        this.dataFile = dataFile;
    }

    public String isLegalStay() {
        Collections.sort(listStays, Comparator.comparing(Stay::getStartStay));
        validationData();
        if (!validDates) {
            return "Не валидные данные!!";
        }
        LocalDateTime start;
        LocalDateTime end;
        LocalDateTime now = LocalDateTime.now();
        for (Stay stay : listStays
                ) {
            start = stay.getStartStay().atStartOfDay();
            if (stay.getEndStay() != null) {
                end = stay.getEndStay().atStartOfDay();
            } else {
                end = now;
            }

            if (Duration.between(start, now).toDays() > 180) {
                start = now.minusDays(180);
            }

            if (Duration.between(end, now).toDays() > 180) {
                continue;
            } else {
                leftDaysInES -= (int) Duration.between(start, end).toDays();
            }
            System.out.println(start + " " + end);
            System.out.println(leftDaysInES);
        }
        return (leftDaysInES >= 0 ? "Осталось " + leftDaysInES + " д." : "Прострочено " + (-leftDaysInES) + " д.");
    }

    public void validationData() {
        for (int i = 1; i < listStays.size(); i++) {
            if (listStays.get(i).getStartStay().isBefore(listStays.get(i - 1).getEndStay())) {
                validDates = false;
                return;
            }
        }
        for (Stay stay : listStays
                ) {
            if (stay.getStartStay().isAfter(stay.getEndStay() == null ? LocalDate.now() : stay.getEndStay())) {
                validDates = false;
                return;
            }
        }
    }
}
