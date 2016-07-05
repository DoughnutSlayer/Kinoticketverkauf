package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Geldbetrag
{
    private static final Pattern _eingabePattern = Pattern
        .compile("\\d++(,|\\.)?+(\\d{1,2}+)?+");
    private static final Pattern _ganzeZahlPattern = Pattern.compile("\\d++");

    private static HashMap<Integer, Geldbetrag> _alleGeldbetraege;

    private final int _gesamterCentbetrag;
    private final String _betragString;

    /**
     * Initialisiert einen neuen Geldbetrag. Es darf noch keinen anderen Geldbetrag mit dem gleichen gesamten Centbetrag geben.
     * 
     * @param gesamterCentbetrag Der gesamte Centbetrag dieses Geldbetrags. D.h. der Wert 1025 steht für 10,25€.
     * 
     * @require !_alleGeldbetraege.containsKey(gesamterCentbetrag) 
     */
    private Geldbetrag(Integer gesamterCentbetrag)
    {
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

    public static Geldbetrag select(String geldBetragString)
    {
        return select(parseEingabe(geldBetragString));
    }

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
    
    @Override
    public int hashCode()
    {
        return ("" + _gesamterCentbetrag).hashCode();
    }

    public Geldbetrag plus(Geldbetrag summand)
    {
        return select(_gesamterCentbetrag + summand._gesamterCentbetrag);
    }

    public Geldbetrag minus(Geldbetrag subtrahend)
    {
        return select(_gesamterCentbetrag - subtrahend._gesamterCentbetrag);
    }

    public Geldbetrag mal(int faktor)
    {
        return select(_gesamterCentbetrag * faktor);
    }

    private static boolean istErlaubterGesamterCentbetrag(int centbetrag)
    {
        return centbetrag >= 0;
    }

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

    private static boolean enthaeltPunkteOderKommata(String string)
    {
        return string.contains(".") || string.contains(",");
    }

    /**
     * @param eingabe Ein String der einen Eurobetrag repräsentiert.
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
     * @require _ganzeZahlPattern.matcher(eingabe).matches()
     */
    private static int liesNatuerlicheZahl(String zahl)
    {
        assert _ganzeZahlPattern.matcher(zahl)
            .matches() : "Vorbedingung verletzt: _ganzeZahlPattern.matcher(eingabe).matches()";
        return Integer.parseUnsignedInt(zahl);
    }

    /**
     * @param kommaBetrag Ein String der einen Kommabetrag repräsentiert.
     * @return Der kommaBetrag als int.
     * 
     * @require istErlaubterString(kommaBetrag)
     */
    private static int liesKommaBetrag(String kommaBetrag)
    {
        assert istErlaubterString(kommaBetrag) : "Vorbedingung verletzt";

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