package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.bezahlwerkzeug;

import java.util.HashSet;
import java.util.Set;

/**
 * Nimmt einen String der einen EuroCentbetrag represäntiert und liefert den gesamten Centbetrag als int-Wert zurück.
 * 
 * @author TMNT
 * @version 26.06.2016
 */
class eingabenParser
{
    private String _punkt = "."; // Eines der Zeichen die in dem String Euro und Cent trennen.
    private String _komma = ","; // Eines der Zeichen die in dem String Euro und Cent trennen.
    private String _ziffern = "1234567890"; // Die Ziffern von 1-9.

    private Set<Character> _erlaubteZeichen; // Alle Zeichen die im eingegebenen String vorkommen dürfen.

    public eingabenParser()
    {
        initialisiereErlaubteZeichen();
    }

    /**
     * Initialisiert das Set mit den erlaubten Zeichen.
     */
    private void initialisiereErlaubteZeichen()
    {
        _erlaubteZeichen = new HashSet<Character>();
        for (char character : (_ziffern + _punkt + _komma).toCharArray())
        {
            _erlaubteZeichen.add(new Character(character));
        }
    }

    /**
     * Ersetzt sämtliche Punkte im String durch Kommata und entfernt Kommata am Ende des Strings.
     * @param eingabe Ein String der eine Dezimahlzahl repräsentiert.
     * @return Der formatierte String.
     */
    public String formatiereEingabe(String eingabe)
    {
        return entferneKommataAmEnde(ersetzePunkteDurchKommata(eingabe));
    }

    /**
     * @param string Der String dessen Punkte und Kommata am Ende entfernt werden sollen.
     * @return Der eingegebene String ohne Punkte oder Kommata am Ende.
     */
    private String entferneKommataAmEnde(String string)
    {
        while (string.endsWith(_komma))
        {
            string = string.substring(0, string.length() - 1);
        }

        return string;
    }

    /** 
     * @param eingabe String der einen EuroCentbetrag represäntiert.
     * @return int-Wert der den gesamten Centbetrag der Eingabe represäntiert. -1 wenn die Eingabe nicht erlaubt ist.
     */
    public int parseEingabe(String eingabe)
    {
        if (sindZeichenErlaubt(eingabe))
        {
            if (!eingabe.contains(_komma))
            {
                return liesEuroBetrag(eingabe);
            }
            else if (istKommaBetrag(eingabe))
            {
                return liesKommaBetrag(eingabe);
            }
        }
        return -1;

    }

    /**
     * @return Der eingegebene String bei dem sämtliche Punkte (".") durch Kommata (",") ersetzt wurden.
     */
    private String ersetzePunkteDurchKommata(String string)
    {
        while (string.contains(_punkt))
        {
            int punktStelle = string.indexOf(_punkt);
            String teilEins = string.substring(0, punktStelle);
            String teilZwei = string.substring(punktStelle + 1,
                    string.length());
            string = teilEins + _komma + teilZwei;
        }
        return string;
    }

    /**
     * @param eingabe Die Eingabe die überprüft werden soll.
     * @return Ob alle in der Eingabe enthaltenen Zeichen erlaubt sind.
     */
    private boolean sindZeichenErlaubt(String eingabe)
    {
        Set<Character> characters = gibCharacterSet(eingabe);

        return _erlaubteZeichen.containsAll(characters);
    }

    /**
     * Versucht die Eingabe direkt zu einem int-Wert umzuwandeln.
     * 
     * @param eingabe Die Eingabe die einen Eurobetrag darstellen soll.
     * @return Der Centbetrag der Eingabe.
     * @throws NumberFormatException Wirft diese Exception, wenn die Eingabe nicht nur Ziffern enthält.
     */
    private int liesEuroBetrag(String eingabe)
    {
        return Integer.parseUnsignedInt(eingabe) * 100;
    }

    /**
     * @param eingabe Die Eingabe, die eine Kommazahl darstellen soll.
     * @return Ob die Eingabe eine Kommazahl darstellt.
     */
    private boolean istKommaBetrag(String eingabe)
    {
        if (!textEnthaeltBuchstabenGenauEinmal(eingabe, _komma.charAt(0)))
        {
            return false;
        }

        String[] gespalteneZahl = eingabe.split(_komma);
        String centString = gespalteneZahl[1];

        if (centString.length() > 2)
        {
            return entferne0enAmEnde(centString).length() <= 2;
        }
        return true;
    }

    /**
     * @param text Ein Text dessen Inhalt überprüft werden soll.
     * @param buchstabe Der Buchstabe nach dem im Text gesucht werden soll.
     * @return Ob der Buchstabe genau einmal im Text vorkommt.
     */
    private boolean textEnthaeltBuchstabenGenauEinmal(String text,
            char buchstabe)
    {
        int i = 0;
        String testString = String.copyValueOf(text.toCharArray());
        String buchstabenString = String.valueOf(buchstabe);

        while (testString.contains(buchstabenString))
        {
            testString = testString.replaceFirst(buchstabenString, "");
            ++i;
        }

        return i == 1;
    }

    /**
     * @return Der eingegegbene String bei dem sämtliche "0" am Ende entfernt wurden.
     */
    private String entferne0enAmEnde(String string)
    {
        while (string.endsWith("0"))
        {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    /**
     * @param kommaBetrag Ein String der einen EuroCentbetrag represäntiert.
     * @return Der EuroCentBetrag als int.
     * 
     * @require istKommaBetrag(KommaBetrag)
     */
    private int liesKommaBetrag(String kommaBetrag)
    {
        assert istKommaBetrag(
                kommaBetrag) : "Vorbedingung verletzt: istKommaBetrag(kommaBetrag)";

        String[] gespalteneZahl = kommaBetrag.split(_komma);
        int euro = 0;
        int cent = 0;

        if (!gespalteneZahl[0].trim()
            .equals(""))
        {
            euro = Integer.parseUnsignedInt(gespalteneZahl[0]);
        }

        String centString = gespalteneZahl[1];

        if (centString.length() > 2)
        {
            centString = entferne0enAmEnde(centString);
        }

        if (centString.length() == 2)
        {
            cent = Integer.parseUnsignedInt(centString);
        }
        else if (centString.length() == 1)
        {
            cent = 10 * Integer.parseUnsignedInt(centString);
        }

        return (euro * 100) + cent;
    }

    /**
     * @return Ein Set welches sämtliche Characters enthält, die in dem String vorkommen.
     */
    private Set<Character> gibCharacterSet(String text)
    {
        HashSet<Character> characters = new HashSet<Character>();

        for (char character : text.toCharArray())
        {
            characters.add(character);
        }

        return characters;
    }
}
