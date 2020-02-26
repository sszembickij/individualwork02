package stayeurope;

import java.time.LocalDate;

class Stay {
    private LocalDate startStay;
    private LocalDate endStay;

    Stay(LocalDate startStay, LocalDate endStay) {
        this.startStay = startStay;
        this.endStay = endStay;
    }

    LocalDate getStartStay() {
        return startStay;
    }

    LocalDate getEndStay() {
        return endStay;
    }
}
