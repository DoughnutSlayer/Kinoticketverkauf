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
 * @version 12.07.2016
 */
public class Geldbetrag
{
    private static final Pattern EINGABE_PATTERN = Pattern
        .compile("(\\d*+)(,|\\.)?+(\\d{0,2}+)"); // Das Pattern zu der regular expressions für Eingaben.

    private static final HashMap<Integer, Geldbetrag> ALLE_GELDBETRÄGE = new HashMap<>(); // Eine Hashmap die jeden erstellten Geldbetrag, mit dessen gesamten Centbetrag als Key, speichert.

    private final int _GESAMTER_CENTBETRAG; // Der gesamte Betrag dieses Geldbetrags in Cent.

    private final String _BETRAG_STRING; // Eine String repräsentation des Geldbetrags in der Form "EE,CC".
    private final int _MAXIMALER_ERLAUBTER_FAKTOR; // Der größte Faktor mit dem dieser Geldbetrag multipliziert werden darf.

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
        _GESAMTER_CENTBETRAG = gesamterCentbetrag;
        _BETRAG_STRING = erstelleBetragString();
        _MAXIMALER_ERLAUBTER_FAKTOR = bestimmeMaximalenErlaubtenFaktor();
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

        geldBetragString = formatiereEingabe(geldBetragString.trim());
        return select(parseEingabe(geldBetragString));
    }

    /**
     * @param eingabe Ein String der einen Geldbetrag darstellen soll.
     * @return Ob dieser String einem Geldbetrag zuzuordnen ist.
     */
    public static boolean istErlaubterString(String eingabe)
    {
        eingabe = eingabe.trim();
        Matcher eingabeMatcher = EINGABE_PATTERN.matcher(eingabe);
        if (!eingabeMatcher.matches())
        {
            return false;
        }

        eingabe = formatiereEingabe(eingabe);
        String eingabeCentGesamt = new StringBuilder(eingabe)
            .deleteCharAt(eingabe.length() - 2)
            .toString();
        return !istZahlZuGroßFuerInt(eingabeCentGesamt);
    }

    /**
     * @param summand Der Geldbetrag, der zu diesem Geldbetrag addiert werden soll.
     * @return Ein Geldbetrag, dessen Wert der Summe von diesem Geldwert und dem Summanden entspricht.
     */
    public Geldbetrag plus(Geldbetrag summand)
    {
        assert summand != null : "Vorbedingung verletzt: summand != null";
        return select(_GESAMTER_CENTBETRAG + summand._GESAMTER_CENTBETRAG);
    }

    /**
     * Der Wert des Subtrahenden darf nicht größer als der des Minuenden sein.
     * @param subtrahend Der Geldbetrag, der von diesem Geldbetrag abgezogen werden soll.
     * @return Ein Geldbetrag, dessen Wert der Differenz von diesem Geldbetrag und dem Subtrahenden entspricht.
     */
    public Geldbetrag minus(Geldbetrag subtrahend)
    {
        assert subtrahend != null : "Vorbedingung verletzt: subtrahen != null";
        return select(_GESAMTER_CENTBETRAG - subtrahend._GESAMTER_CENTBETRAG);
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
        return select(_GESAMTER_CENTBETRAG * faktor);
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
        return _GESAMTER_CENTBETRAG > anderer._GESAMTER_CENTBETRAG;
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
        return _GESAMTER_CENTBETRAG >= anderer._GESAMTER_CENTBETRAG;
    }

    /**
     * @param faktor Der Faktor mit dem dieser Geldbetrag multipliziert werden soll.
     * @return Ob der Faktor mit diesem Geldwert multipliziert werden darf.
     */
    public boolean istErlaubterFaktor(int faktor)
    {
        return faktor >= 0 && faktor <= _MAXIMALER_ERLAUBTER_FAKTOR;
    }

    @Override
    public String toString()
    {
        return _BETRAG_STRING;
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
     * Wandelt einen String, der als Betrag erlaubt ist, um in die Form "EE,CC".
     * @param Der String, der umgewandelt werden soll.
     * @return Der umgewandelte String in der Form "EE,CC".
     */
    private static String formatiereEingabe(String eingabe)
    {
        Matcher eingabeMatcher = EINGABE_PATTERN.matcher(eingabe);
        eingabeMatcher.matches();

        String eingabeEuro = eingabeMatcher.group(1);
        String eingabeCent = eingabeMatcher.group(3);

        String ergebnisEuro = "0";
        String ergebnisCent = "00";

        if (!eingabeEuro.equals(""))
        {
            ergebnisEuro = eingabeEuro;
        }

        if (!eingabeCent.equals(""))
        {
            String optionale0 = (eingabeCent.length() == 1) ? "0" : "";
            ergebnisCent = eingabeCent + optionale0;
        }

        return ergebnisEuro + "," + ergebnisCent;
    }

    /**
     * @param eingabe Ein String in der Form "EE,CC". 
     * @return Ein Integer Wert der Form EECC.
     */
    private static Integer parseEingabe(String eingabe)
    {
        Matcher eingabeMatcher = EINGABE_PATTERN
            .matcher(formatiereEingabe(eingabe));
        eingabeMatcher.matches();

        return Integer
            .parseInt(eingabeMatcher.group(1) + eingabeMatcher.group(3));
    }

    /**
     * Bestimmt ob ein String, der nur aus Ziffern besteht, einen größeren Wert hat, als Integer.MAX_VALUE.
     * 
     * @param zahl Der String, der überprüft werden soll.
     * @return Ob der Wert von zahl größer ist, als Integer.MAX_VALUE.
     * 
     * @require zahl.matches("\\d*")
     */
    private static boolean istZahlZuGroßFuerInt(String zahl)
    {
        if (zahl.length() != 10)
        {
            return zahl.length() > 10;
        }

        char[] ziffern = zahl.toCharArray();
        char[] integerMaxZiffern = String.valueOf(Integer.MAX_VALUE)
            .toCharArray();
        for (int i = 0; i < 10; i++)
        {
            if (ziffern[i] != integerMaxZiffern[i])
            {
                return ziffern[i] > integerMaxZiffern[i];
            }
        }
        return false;
    }

    /**
     * @return Ein String der den Wert dieses Geldbetrags darstellt. Dieser hat die Form "EE,CC".
     */
    private String erstelleBetragString()
    {
        String euroString = "" + _GESAMTER_CENTBETRAG / 100;
        String optionale0 = (_GESAMTER_CENTBETRAG % 100 >= 10) ? "" : "0";
        String centString = optionale0 + _GESAMTER_CENTBETRAG;

        return euroString + ',' + centString;
    }

    /**
     * @return Der maximale Faktor mit dem dieser Gedlbetrag multipliziert werden kann.
     */
    private int bestimmeMaximalenErlaubtenFaktor()
    {
        if (_GESAMTER_CENTBETRAG == 0)
        {
            return Integer.MAX_VALUE;
        }
        return Integer.MAX_VALUE / _GESAMTER_CENTBETRAG;
    }
}