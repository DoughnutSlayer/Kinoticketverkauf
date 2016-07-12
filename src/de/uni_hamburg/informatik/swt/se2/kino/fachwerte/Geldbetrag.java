package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ein Geldbetrag steht für eine bestimmte Geldsumme, er kann nicht negativ sein.
 * Man kann sich dessen Wert als String in einem geeigneten Format ausgeben lassen.
 * Man kann Geldwerte entweder mit einem Integer bzw. int-Wert auswählen, sowie mit einem geeignet formatierten String.
 * 
 * @author TMNT
 * @version 05.07.2016
 */
public class Geldbetrag
{
    private static final Pattern EINGABE_PATTERN = Pattern
        .compile("(\\d*+)(,|\\.)?+(\\d{0,2}+)"); // Das Pattern zu der regular expressions für Eingaben.

    private static final HashMap<Integer, Geldbetrag> ALLE_GELDBETRÄGE = new HashMap<>(); // Eine Hashmap die jeden erstellten Geldbetrag, mit dessen gesamten Centbetrag als Key, speichert.

    private final int _gesamterCentbetrag; // Der gesamte Betrag dieses Geldbetrags in Cent.

    private final String _betragString; // Eine String repräsentation des Geldbetrags in der Form "EE,CC".
    private final int _maximalerErlaubterFaktor; // Der größte Faktor mit dem dieser Geldbetrag multipliziert werden darf.

    /**
     * Initialisiert einen neuen Geldbetrag. Es darf noch keinen anderen Geldbetrag mit dem gleichen gesamten Centbetrag geben.
     * Außerdem muss der Centbetrag positiv sein.
     * 
     * @param gesamterCentbetrag Der gesamte Centbetrag dieses Geldbetrags. D.h. der Wert 1025 steht für 10,25€.
     * 
     * @require istErlaubterGesamterCentbetrag(gesamterCentbetrag)
     * @require !_alleGeldbetraege.containsKey(gesamterCentbetrag) 
     */
    private Geldbetrag(Integer gesamterCentbetrag)
    {
        assert istErlaubterGesamterCentbetrag(
                gesamterCentbetrag) : "Vorbedingung verletzt: istErlaubterGesamterCentbetrag(gesamterCentbetrag)";
        assert !ALLE_GELDBETRÄGE.containsKey(
                gesamterCentbetrag) : "Vorbedingung verletzt: !_alleGeldbeträge.containsKey(gesamterCentbetrag)";

        _gesamterCentbetrag = gesamterCentbetrag;
        _betragString = erstelleBetragString();
        _maximalerErlaubterFaktor = bestimmeMaximalenErlaubtenFaktor();
        ALLE_GELDBETRÄGE.put(gesamterCentbetrag, this);
    }

    /**
     * @param centbetrag Der gesamte Centbetrag des gewünschten Geldbetrags. Der Centbetrag darf nicht negativ sein.
     * @return Der Geldbetrag dessen gesamter Centbetrag dem gegebenen Centbetrag entspricht.
     * 
     * @require istErlaubterGesamterCentbetrag(centbetrag)
     */
    public static Geldbetrag select(Integer centbetrag)
    {
        assert istErlaubterGesamterCentbetrag(
                centbetrag) : "Vorbedingung verletzt: istErlaubterGesamterCentbetrag(gesamterCentbetrag)";

        if (ALLE_GELDBETRÄGE.containsKey(centbetrag))
        {
            return ALLE_GELDBETRÄGE.get(centbetrag);
        }

        Geldbetrag neuerGeldbetrag = new Geldbetrag(centbetrag);
        return neuerGeldbetrag;
    }

    /**
     * @param geldBetragString Ein String in der Form "EE,CC".
     * @return Der Geldbetrag dessen Wert dem String entspricht.
     */
    public static Geldbetrag select(String geldBetragString)
    {
        assert istErlaubterString(
                geldBetragString) : "Vorbedingung verletzt: istErlaubterString(centbetragString)";

        geldBetragString = geldBetragString.trim();
        return select(parseEingabe(geldBetragString));
    }

    /**
     * @param eingabe Ein String der einen Geldbetrag darstellen soll.
     * @return Ob dieser String einem Geldbetrag zuzuordnen ist.
     */
    public static boolean istErlaubterString(String eingabe) //TODO: zu große ints nicht erlauben
    {
        eingabe = eingabe.trim();
        return EINGABE_PATTERN.matcher(eingabe)
            .matches();
    }

    @Override
    public String toString()
    {
        return _betragString;
    }

    /**
     * @param summand Der Geldbetrag, der zu diesem Geldbetrag addiert werden soll.
     * @return Ein Geldbetrag, dessen Wert der Summe von diesem Geldwert und dem Summanden entspricht.
     */
    public Geldbetrag plus(Geldbetrag summand)
    {
        assert summand != null : "Vorbedingung verletzt: summand != null";
        return select(_gesamterCentbetrag + summand._gesamterCentbetrag);
    }

    /**
     * Der Wert des Subtrahenden darf nicht größer als der des Minuenden sein.
     * @param subtrahend Der Geldbetrag, der von diesem Geldbetrag abgezogen werden soll.
     * @return Ein Geldbetrag, dessen Wert der Differenz von diesem Geldbetrag und dem Subtrahenden entspricht.
     */
    public Geldbetrag minus(Geldbetrag subtrahend)
    {
        assert subtrahend != null : "Vorbedingung verletzt: subtrahen != null";
        return select(_gesamterCentbetrag - subtrahend._gesamterCentbetrag);
    }

    /**
     * @param faktor Eine ganzzahlige, positive Zahl. Der Faktor darf nicht so groß sein, dass der Wert des Ergebnisses größer ist, als Integer.MAX_VALUE.
     * @return Ein Geldbetrag, dessen Wert dem Produkt von diesem Geldwert mit dem Faktor entspricht.
     * 
     * @require istErlaubterFaktor(faktor)
     */
    public Geldbetrag mal(int faktor)
    {
        assert istErlaubterFaktor(
                faktor) : "Vorbedingung verletzt: istErlaubterFaktor(faktor)";
        return select(_gesamterCentbetrag * faktor);
    }

    /**
     * @param anderer Der Geldbetrag mit dem dieser Geldbetrag Verglichen werden soll.
     * @return Ob dieser Geldbetrag größer ist, als der übergebene Geldbetrag.
     * 
     * @require anderer != null
     */
    public boolean groesserAls(Geldbetrag anderer)
    {
        assert anderer != null : "Vorbedingung verletzt: anderer != null";
        return _gesamterCentbetrag > anderer._gesamterCentbetrag;
    }

    /**
     * @param anderer Der Geldbetrag mit dem dieser Geldbetrag Verglichen werden soll.
     * @return Ob dieser Geldbetrag größer oder gleich dem anderen Geldbetrag ist.
     * 
     * @require anderer != null
     */
    public boolean groesserGleich(Geldbetrag anderer)
    {
        assert anderer != null : "Vorbedingung verletzt: anderer != null";
        return _gesamterCentbetrag >= anderer._gesamterCentbetrag;
    }

    /**
     * @param anderer Der Centbetrag mit dem Verglichen werden soll.
     * @return Ob dieser Geldbetrag größer ist, als der übergebene Centbetrag.
     * 
     * @require istErlaubterGesamterCentbetrag(centbetrag)
     */
    public boolean groesserAls(int centbetrag)
    {
        assert istErlaubterGesamterCentbetrag(
                centbetrag) : "Vorbedingung verletzt: istErlaubterGesamterCentbetrag(centbetrag)";
        return _gesamterCentbetrag > centbetrag;
    }

    /**
     * @param anderer Der Centbetrag mit dem Verglichen werden soll.
     * @return Ob dieser Geldbetrag größer oder gleich dem übergebenen Centbetrag ist.
     * 
     * @require istErlaubterGesamterCentbetrag(centbetrag)
     */
    public boolean groesserGleich(int centbetrag)
    {
        assert istErlaubterGesamterCentbetrag(
                centbetrag) : "Vorbedingung verletzt: istErlaubterGesamterCentbetrag(centbetrag)";
        return _gesamterCentbetrag >= centbetrag;
    }

    /**
     * @param centbetrag Ein Centbetrag.
     * @return Ob es einen Geldwert geben kann, dessen Wert diesem Centbetrag entspricht.
     */
    private static boolean istErlaubterGesamterCentbetrag(int centbetrag)
    {
        return centbetrag >= 0;
    }

    /**
     * @param eingabe Ein Text in der Form "EE,CC". 
     * @return Ein Integer Wert der Form EECC.
     */
    private static Integer parseEingabe(String eingabe)
    {
        if (eingabe.equals(""))
        {
            return 0;
        }

        Matcher eingabeMatcher = EINGABE_PATTERN.matcher(eingabe);
        eingabeMatcher.matches();

        if (!eingabeIstKommazahl(eingabeMatcher))
        {
            return liesEuroBetrag(eingabe);
        }

        return liesKommaBetrag(eingabeMatcher);
    }

    /**
     * @param eingabeMatcher Ein Matcher, der eine valide Eingabe gegen das Eingabepattern gematcht hat.
     * @return Ob die Eingabe ein Punkt oder Komma enthaelt.
     * 
     * @require eingabeMatcher.matches()
     */
    private static boolean eingabeIstKommazahl(Matcher eingabeMatcher)
    {
        return eingabeMatcher.group(2) != null;
    }

    /**
     * @param eingabe Ein String der einen Eurobetrag repräsentiert. Der String darf nur Ziffern enthalten.
     * @return Der Wert des Eurobetrags in Cent.
     */
    private static Integer liesEuroBetrag(String eingabe)
    {
        return liesNatuerlicheZahl(eingabe) * 100;
    }

    /**
     * @param zahl Ein String der nur Ziffern enthält.
     * @return Die Zahl, welche von der Eingabe dargestellt wird.
     * 
     * @require _natuerlicheZahlPattern.matcher(eingabe).matches()
     */
    private static int liesNatuerlicheZahl(String zahl)
    {
        return Integer.parseInt(zahl);
    }

    /**
     * @param kommaBetrag Ein String der einen Kommabetrag repräsentiert.
     * @return Der kommaBetrag als int-Wert.
     * 
     * @require istErlaubterString(kommaBetrag)
     */
    private static int liesKommaBetrag(Matcher eingabeMatcher)
    {
        String euroString = eingabeMatcher.group(1);
        String centString = eingabeMatcher.group(3);

        int euro = 0;
        int cent = 0;

        if (!"".equals(euroString))
        {
            euro = liesNatuerlicheZahl(euroString);
        }

        if (centString.length() == 1)
        {
            cent = 10 * liesNatuerlicheZahl(centString);
        }
        else if (centString.length() == 2)
        {
            cent = liesNatuerlicheZahl(centString);
        }

        return (euro * 100) + cent;
    }

    /**
     * @return Ein String der den Wert dieses Geldbetrags darstellt. Dieser hat die Form "EE,CC".
     */
    private String erstelleBetragString()
    {
        String betragString = _gesamterCentbetrag / 100 + ","
                + _gesamterCentbetrag % 100;
        if (_gesamterCentbetrag % 100 < 10)
        {
            betragString = betragString.substring(0, betragString.length() - 1)
                    + "0" + _gesamterCentbetrag % 100;
        }

        return betragString;
    }

    /**
     * @return Der maximale Faktor mit dem dieser Gedlbetrag multipliziert werden kann.
     */
    private int bestimmeMaximalenErlaubtenFaktor()
    {
        if (_gesamterCentbetrag == 0)
        {
            return Integer.MAX_VALUE;
        }
        return Integer.MAX_VALUE / _gesamterCentbetrag;
    }

    /**
     * @param faktor Der Faktor mit dem dieser Geldbetrag multipliziert werden soll.
     * @return Ob der Faktor mit diesem Geldwert multipliziert werden darf.
     */
    public boolean istErlaubterFaktor(int faktor)
    {
        return faktor >= 0 && faktor <= _maximalerErlaubterFaktor;
    }
}