package stayeurope;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Stay {
    private LocalDate startStay;
    private LocalDate endStay;

    public Stay(String string) {
        try {
            String[] arr = string.split("[-]");
            this.startStay = LocalDate.parse(arr[0].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (arr.length == 2) {
                this.endStay = LocalDate.parse(arr[1].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LocalDate getStartStay() {
        return startStay;
    }

    public LocalDate getEndStay() {
        return endStay;
    }
}
