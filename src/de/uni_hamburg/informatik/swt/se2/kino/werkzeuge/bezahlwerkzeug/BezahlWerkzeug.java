package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.bezahlwerkzeug;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * Eine Klasse die den Verkauf von Kinotickets abwickelt.
 * 
 * @author TMNT
 * @version 26.06.2016
 */
public class BezahlWerkzeug
{
    private int _preis; // Der Gesamtpreis der zu bezahlenden Kinotickets.  //TODO: Geldbetrag implementieren.

    private boolean _bezahlt; // Ob die Kinotickets bezahlt wurden.

    private BezahlWerkzeugUI _ui; // Das UI dieses Werkzeugs.

    private eingabenParser _parser; // Ein parser der die Eingaben aus dem Textfeld des UIs in Centbeträge umwandelt.

    /**
     * Initialisiert den Parser.
     */
    public BezahlWerkzeug()
    {
        _parser = new eingabenParser();
    }

    /**
     * Eröffnet den Bezahldialog mit dem User.
     * @param preis Der Gesamtpreis der zu verkaufenden Kinotickets.
     * @return Ob die Kinotickets bezahlt wurden.
     * 
     * @require preis >= 0
     */
    public boolean rufeBezahlDialogAuf(int preis)   //TODO: Geldbetrag implementieren.
    {
        assert preis >= 0 : "Vorbedingung verletzt: preis >= 0";

        _preis = preis;

        _ui = new BezahlWerkzeugUI();
        _ui.getPreisFeld()
            .setText(erstelleBetragString(_preis));
        aktualisiereUI(0);
        registriereUIAktionen();

        _ui.oeffneDialog();

        return _bezahlt;
    }

    /**
     * @param betrag Ein Centbetrag der zu einem String umgewandelt werden soll.
     * @return Ein String der einen EuroCent Betrag repräsentiert in der Form "10,25€".
     */
    private String erstelleBetragString(int betrag)     //TODO: Geldbetrag implementieren.
    {
        String euroCent = String.valueOf(betrag);
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

        return euro + "," + cent + "€";
    }

    /**
     * Fügt die Funktionalität zu den Buttons und dem Eingabefeld des UIs hinzu.
     */
    private void registriereUIAktionen()
    {
        _ui.getOkButton()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _bezahlt = true;
                    _ui.schliesseDialog();
                }
            });

        _ui.getAbbrechenButton()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _bezahlt = false;
                    _ui.schliesseDialog();
                }
            });

        _ui.getEingabefeld()
            .getDocument()
            .addDocumentListener(new DocumentListener()
            {
                @Override
                public void changedUpdate(DocumentEvent e)
                {
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    reagiereAufEingabe(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    reagiereAufEingabe(e);
                }
            });
    }

    /**
     * Wenn der User etwas eingegeben hat, gibt diese Methode die Eingabe an den Parser weiter und aktualisiert das UI entsprechend.
     * @param e Das DocumentEvent das von dem Eingabefeld gesendet wurde.
     */
    private void reagiereAufEingabe(DocumentEvent e)    //TODO: Geldbetrag implementieren.
    {
        String eingabe = "";
        try
        {
            eingabe = e.getDocument()
                .getText(0, e.getDocument()
                    .getLength());
        }
        catch (BadLocationException excptn)
        {
        }
        eingabe = eingabe.trim();
        eingabe = _parser.formatiereEingabe(eingabe);

        if (eingabe.equals(""))
        {
            aktualisiereUI(0);
        }
        else
        {
            aktualisiereUI(_parser.parseEingabe(eingabe));
        }
    }

    /**
     * Aktualisiert das UI entsprechend dem eingegebenen Betrag.
     * @param gegeben Der vom Kunden gegebene Centbetrag.
     */
    private void aktualisiereUI(int gegeben)    //TODO: Geldbetrag implementieren.
    {
        int rückgeld = gegeben - _preis;
        boolean genugGegeben = rückgeld >= 0;
        Label rueckgeldLabel = _ui.getRueckgeldLabel();
        Label rueckgeldFeld = _ui.getRueckgeldFeld();

        if (rückgeld >= 0)
        {
            rueckgeldLabel.setText("Rückgeld:   ");
            rueckgeldFeld.setText(erstelleBetragString(rückgeld));
        }
        else if (gegeben < 0)
        {
            rueckgeldLabel.setText("");
            rueckgeldFeld.setText("Ungültige Eingabe");
        }
        else
        {
            rueckgeldLabel.setText("Noch zu zahlen:   ");
            rueckgeldFeld.setText(erstelleBetragString(-rückgeld));
        }

        _ui.getOkButton()
            .setEnabled(genugGegeben);
    }
}
