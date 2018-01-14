package com.plushundred.nils.pilotlog;

/**
 * Created by Nils on 8/3/17.
 */

public class FlightLog {

    String date, aircraft, ident, from, to, notes;
    Double dualDayHours, dualNightHours, soloDayHours, soloNightHours;
    Boolean crosscountry, simulatedInstrument;
    Integer dayLandings, nightLandings;

    public FlightLog(String date, String aircraft, String ident, String from, String to, Double dualDayHours, Double dualNightHours,
                     Double soloDayHours, Double soloNightHours, Boolean crosscountry, Boolean simulatedInstrument, int dayLandings,
                     int nightLandings, String notes) {

        this.date = date;
        this.aircraft = aircraft;
        this.ident = ident;
        this.from = from;
        this.to = to;
        this.dualDayHours = dualDayHours;
        this.dualNightHours = dualNightHours;
        this.soloDayHours = soloDayHours;
        this.soloNightHours = soloNightHours;
        this.crosscountry = crosscountry;
        this.simulatedInstrument = simulatedInstrument;
        this.dayLandings = dayLandings;
        this.nightLandings = nightLandings;
        this.notes = notes;
    }

    public FlightLog() {
        this.date = "";
        this.aircraft = "";
        this.ident = "";
        this.from = "";
        this.to = "";
        this.dualDayHours = 0.;
        this.dualNightHours = 0.;
        this.soloDayHours = 0.;
        this.soloNightHours = 0.;
        this.crosscountry = false;
        this.simulatedInstrument = false;
        this.dayLandings = 0;
        this.nightLandings = 0;
        this.notes = "";

    }

    public String getDate() {
        return date;
    }

    public String getAircraft() {
        return aircraft;
    }

    public String getIdent() {
        return ident;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getDualDayHours() {
        return dualDayHours;
    }

    public Double getDualNightHours() {
        return dualNightHours;
    }

    public Boolean getCrosscountry() {
        return crosscountry;
    }

    public Double getSoloDayHours() {
        return soloDayHours;
    }

    public Double getSoloNightHours() {
        return soloNightHours;
    }

    public Boolean getSimulatedInstrument() {
        return simulatedInstrument;
    }

    public Integer getDayLandings() {
        return dayLandings;
    }

    public Integer getNightLandings() {
        return nightLandings;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDualDayHours(Double dualDayHours) {
        this.dualDayHours = dualDayHours;
    }

    public void setDualNightHours(Double dualNightHours) {
        this.dualNightHours = dualNightHours;
    }

    public void setCrosscountry(Boolean crosscountry) {
        this.crosscountry = crosscountry;
    }

    public void setSoloDayHours(Double soloDayHours) {
        this.soloDayHours = soloDayHours;
    }

    public void setSoloNightHours(Double soloNightHours) {
        this.soloNightHours = soloNightHours;
    }

    public void setSimulatedInstrument(Boolean simulatedInstrument) {
        this.simulatedInstrument = simulatedInstrument;
    }

    public void setDayLandings(Integer dayLandings) {
        this.dayLandings = dayLandings;
    }

    public void setNightLandings(Integer nightLandings) {
        this.nightLandings = nightLandings;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getTotalHours() {
        return getDualDayHours() + getDualNightHours() + getSoloDayHours() + getSoloNightHours();
    }
}

