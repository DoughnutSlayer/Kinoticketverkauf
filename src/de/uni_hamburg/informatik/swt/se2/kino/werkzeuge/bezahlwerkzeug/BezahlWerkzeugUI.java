package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.bezahlwerkzeug;

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;;

/**
 * Das UI des {@link BezahlWerkzeug}
 * 
 * @author TMNT
 * @version 26.06.2016
 */
class BezahlWerkzeugUI
{
    private JDialog _dialog; // Das Dialogfenster.

    private JTextField _eingabefeld; // Das Eingabefeld für den übergebenen Betrag.

    private JButton _okButton; // Der "Ok" Knopf in dem Dialogfenster.
    private JButton _abbrechenButton; // Der "Abbrechen" Knopf in dem Dialogfenster.

    private Label _preis; // Das Label auf dem der Preis der zu bezahlenden  Kinotickets angezeigt wird.
    private Label _rueckgeld; // Das Label auf dem das Rückgeld, noch zu Zahlende Beträge und Fehlerachrichten an den User angezeigt werden.
    private Label _rueckgeldLabel; // Dieses Label zeigt Kontext für das "_rueckgeld" Label an.

    private final int BREITE = 250; // Die Breite des Dialogfensters.
    private final int HOEHE = 150; // Die Höhe des Dialogfensters.

    /**
     * Erstellt ein neues Bezahldialogfenster, welches zunächst unsichtbar ist.
     */
    public BezahlWerkzeugUI()
    {
        erstelleDialog();

        Container gridContainer = erstelleGrid();
        Container buttonContainer = erstelleButtons();

        _dialog.getContentPane()
            .add(gridContainer);
        _dialog.getContentPane()
            .add(buttonContainer);
    }

    /**
     * Hilfsmethode die das Dialogfenster initialisiert.
     */
    private void erstelleDialog()
    {
        _dialog = new JDialog();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode()
            .getWidth();
        int screenHeight = gd.getDisplayMode()
            .getHeight();
        int xPosition = (screenWidth - BREITE) / 2;
        int yPosition = (screenHeight - HOEHE) / 2;

        _dialog.setBounds(xPosition, yPosition, BREITE, HOEHE);
        _dialog.setResizable(false);

        _dialog.setTitle("Kasse");
        _dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        _dialog.getContentPane()
            .setLayout(
                    new BoxLayout(_dialog.getContentPane(), BoxLayout.Y_AXIS));
    }

    /**
     * @return Ein Container in dem die Labels und das Eingabefenster angeordnet sind.
     */
    private Container erstelleGrid()
    {
        Container grid = new Container();
        _eingabefeld = new JTextField();
        _preis = new Label();
        _rueckgeld = new Label();
        _rueckgeldLabel = new Label();

        Label preisLabel = new Label("Preis:   ");
        Label gegebenLabel = new Label("Gegeben:   ");

        _rueckgeldLabel.setAlignment(Label.RIGHT);
        preisLabel.setAlignment(Label.RIGHT);
        gegebenLabel.setAlignment(Label.RIGHT);

        grid.setLayout(new GridLayout(3, 2));

        grid.add(preisLabel);
        grid.add(_preis);
        grid.add(gegebenLabel);
        grid.add(erstelleEingabefeldContainer());
        grid.add(_rueckgeldLabel);
        grid.add(_rueckgeld);
        grid.setVisible(true);

        return grid;
    }

    /**
     * @return Ein Container der das Eingabefeld sowie ein "€"-Label rechts davon enthält.
     */
    private Container erstelleEingabefeldContainer()
    {
        Container eingabefeldContainer = new Container();
        Label euroLabel = new Label("€");
        LayoutManager mgr = new BoxLayout(eingabefeldContainer,
                BoxLayout.X_AXIS);

        eingabefeldContainer.setLayout(mgr);
        eingabefeldContainer.add(_eingabefeld);
        eingabefeldContainer.add(euroLabel);

        return eingabefeldContainer;
    }

    /**
     * @return Ein Container in dem die Buttons angeordnet sind.
     */
    private Container erstelleButtons()
    {
        Container buttons = new Container();
        _okButton = new JButton("Ok");
        _abbrechenButton = new JButton("Abbrechen");

        buttons.setLayout(new FlowLayout());
        buttons.add(_okButton);
        buttons.add(_abbrechenButton);

        buttons.setSize(BREITE, _okButton.getHeight());

        return buttons;
    }

    /**
     * Macht das Dialogfenster sichtbar.
     */
    public void oeffneDialog()
    {
        _dialog.setVisible(true);
    }

    /**
     * @return Der "Ok"-Button des Dialogfensters.
     */
    public JButton getOkButton()
    {
        return _okButton;
    }

    /**
     * @return Der "Abbrechen"-Button des Dialogfensters.
     */
    public JButton getAbbrechenButton()
    {
        return _abbrechenButton;
    }

    /**
     * @return Das Eingabelf für den übergebenen Betrag.
     */
    public JTextField getEingabefeld()
    {
        return _eingabefeld;
    }

    /**
     * @return Das Label auf dem der Preis der ausgewählten Kinotickets angezeigt wird.
     */
    public Label getPreisFeld()
    {
        return _preis;
    }

    /**
     * @return Das Label auf dem das Rückgeld, noch zu Zahlende Beträge und Fehlerachrichten an den User angezeigt werden.
     */
    public Label getRueckgeldFeld()
    {
        return _rueckgeld;
    }

    /**
     * @return Dieses Label zeigt Kontext für das "_rueckgeld" Label an.
     */
    public Label getRueckgeldLabel()
    {
        return _rueckgeldLabel;
    }

    /**
     * Schließt das Dialogfenster.
     */
    public void schliesseDialog()
    {
        _dialog.dispose();
    }
}
