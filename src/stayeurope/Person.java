package stayeurope;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Person {
    private String surname;
    private String dataFile;
    private int leftDaysInES = 90;
    private boolean validDates = true;
    private List<Stay> listStays = new ArrayList<>();

    private void initListStay() {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] arr = currentLine.split("[-]");
                    listStays.add(new Stay(LocalDate.parse(arr[0].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            arr.length == 2 ? LocalDate.parse(arr[1].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                    : LocalDate.now()));
            }
        } catch (Exception e) {
            validDates = false;
        }
    }

    Person(String surname, String dataFile) {
        this.surname = surname;
        this.dataFile = dataFile;
    }

    String isLegalStay() {
        initListStay();
        listStays.sort(Comparator.comparing(Stay::getStartStay));
        validationData();
        if (!validDates) {
            return "Не валидные данные!!";
        }
        LocalDateTime start;
        LocalDateTime end;
        LocalDateTime now = LocalDate.now().atStartOfDay();
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

    private void validationData() {
        LocalDate now = LocalDate.now();
        for (int i = 1; i < listStays.size(); i++) {
            if (listStays.get(i).getStartStay().isBefore(listStays.get(i - 1).getEndStay())) {
                validDates = false;
                return;
            }
        }
        for (Stay stay : listStays
                ) {
            if (stay.getStartStay().isAfter(stay.getEndStay())
                    || stay.getStartStay().isAfter(now)
                    || stay.getEndStay().isAfter(now)) {
                validDates = false;
                return;
            }
        }
    }
}
