package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Geldbetrag
{
    private static final Pattern _eingabePattern = Pattern
        .compile("\\d++(,|\\.)?+(\\d{1,2}+)?+"); // Das Pattern zu der regular expressions für Eingaben.
    private static final Pattern _natuerlicheZahlPattern = Pattern
        .compile("\\d++"); // Ein Pattern das alle natürlichen Zahlen zulässt.

    private static HashMap<Integer, Geldbetrag> _alleGeldbetraege; // Eine Hashmap die jeden erstellten Geldbetrag, mit dessen gesamten Centbetrag als Key, speichert.

    private final int _gesamterCentbetrag; // Der gesamte Betrag dieses Geldbetrags in Cent.
    private final String _betragString; // Eine String repräsentation des Geldbetrags in der Form "EE,CC".

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
        assert !_alleGeldbetraege.containsKey(
                gesamterCentbetrag) : "Vorbedingung verletzt: !_alleGeldbeträge.containsKey(gesamterCentbetrag)";

        _gesamterCentbetrag = gesamterCentbetrag;
        _betragString = erstelleBetragString();
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

        if (_alleGeldbetraege.containsKey(centbetrag))
        {
            return _alleGeldbetraege.get(centbetrag);
        }

        Geldbetrag neuerGeldbetrag = new Geldbetrag(centbetrag);
        _alleGeldbetraege.put(centbetrag, neuerGeldbetrag);
        return neuerGeldbetrag;
    }

    /**
     * @param geldBetragString Ein String in der Form "EE,CC".
     * @return Der Geldbetrag dessen Wert dem String entspricht.
     */
    public static Geldbetrag select(String geldBetragString)
    {
        return select(parseEingabe(geldBetragString));
    }

    /**
     * @param eingabe Ein String der einen Geldbetrag darstellen soll.
     * @return Ob dieser String einem Geldbetrag zuzuordnen ist.
     */
    public static boolean istErlaubterString(String eingabe)
    {
        return _eingabePattern.matcher(eingabe)
            .matches();
    }

    @Override
    public String toString()
    {
        return _betragString;
    }

    /**
     * Es darf immer nur einen Geldbetrag mit dem gleichen Wert geben.
     * Daher sind zwei Referenzen auf einen Geldbetrag gleich, wenn sie auf den selben Geldbetrag verweisen. 
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Geldbetrag))
        {
            return false;
        }

        Geldbetrag anderer = (Geldbetrag) obj;
        return anderer == this;
    }

    /**
     * Der HashCode basiert auf dem Wert des Geldbetrages.
     */
    @Override
    public int hashCode()
    {
        return ("" + _gesamterCentbetrag).hashCode();
    }

    /**
     * @param summand Der Geldbetrag, der zu diesem Geldbetrag addiert werden soll.
     * @return Ein Geldbetrag, dessen Wert der Summe von diesem Geldwert und dem Summanden entspricht.
     */
    public Geldbetrag plus(Geldbetrag summand)
    {
        return select(_gesamterCentbetrag + summand._gesamterCentbetrag);
    }

    /**
     * Der Wert des Subtrahenden darf nicht größer als der des Minuenden sein.
     * @param subtrahend Der Geldbetrag, der von diesem Geldbetrag abgezogen werden soll.
     * @return Ein Geldbetrag, dessen Wert der Differenz von diesem Geldbetrag und dem Subtrahenden entspricht.
     */
    public Geldbetrag minus(Geldbetrag subtrahend)
    {
        return select(_gesamterCentbetrag - subtrahend._gesamterCentbetrag);
    }

    /**
     * @param faktor Eine ganzzahlige, positive Zahl.
     * @return Ein Geldbetrag, dessen Wert dem Produkt von diesem Geldwert mit dem Faktor entspricht.
     */
    public Geldbetrag mal(int faktor)
    {
        return select(_gesamterCentbetrag * faktor);
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
        assert istErlaubterString(
                eingabe) : "Vorbedingung verletzt: istErlaubterString(centbetragString)";

        if (!enthaeltPunkteOderKommata(eingabe))
        {
            return liesEuroBetrag(eingabe);
        }

        return liesKommaBetrag(eingabe);
    }

    /**
     * @param string Ein String der überprüft werden soll.
     * @return Ob der eingegebene String Punkte (".") oder Kommata (",") enthält.
     */
    private static boolean enthaeltPunkteOderKommata(String string)
    {
        if (string == null)
        {
            return false;
        }
        return string.contains(".") || string.contains(",");
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
        assert _natuerlicheZahlPattern.matcher(zahl)
            .matches() : "Vorbedingung verletzt: _natuerlicheZahlPattern.matcher(eingabe).matches()";
        return Integer.parseUnsignedInt(zahl);
    }

    /**
     * @param kommaBetrag Ein String der einen Kommabetrag repräsentiert.
     * @return Der kommaBetrag als int-Wert.
     * 
     * @require istErlaubterString(kommaBetrag)
     */
    private static int liesKommaBetrag(String kommaBetrag)
    {
        assert istErlaubterString(
                kommaBetrag) : "Vorbedingung verletzt: istErlaubterString(kommaBetrag)";

        String[] gespalteneZahl = kommaBetrag.split(",|\\.");
        int euro = 0;
        int cent = 0;

        switch (gespalteneZahl.length)
        {
        case 0:
            return 0;
        case 1:
            return liesEuroBetrag(gespalteneZahl[0]);
        }

        String euroString = gespalteneZahl[0];
        String centString = gespalteneZahl[1];

        if (!euroString.trim()
            .equals(""))
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
        String euroCent = "" + _gesamterCentbetrag;
        String euro = "0";
        String cent = "00";
        int ziffernAnzahl = euroCent.length();

        if (ziffernAnzahl <= 2)
        {
            cent = cent.substring(ziffernAnzahl) + euroCent;
        }
        else
        {
            euro = euroCent.substring(0, ziffernAnzahl - 2);
            cent = euroCent.substring(ziffernAnzahl - 2);
        }

        return euro + "," + cent;
    }
}