import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.HeadlessException;

public class Execution3 {

    private static int AX, BX, CX, DX;


    private static int[] RAM = new int[256];


    private static boolean SifirBayrak;
    private static boolean IsaretBayrak;


    private static ArrayList<String> program = new ArrayList<>();
    private static HashMap<String, Integer> labels = new HashMap<>();
    private static int programCounter = 0;
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        String sourcePath = null;

        if (args.length >= 1) {

            sourcePath = args[0];
        } else {

            try {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                         | IllegalAccessException ignored) {

                }

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Kaynak dosyayı seçin");
                chooser.setCurrentDirectory(new java.io.File("."));


                javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
                        "Text Files (*.txt)", "txt");
                chooser.setFileFilter(filter);

                int result = chooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    sourcePath = chooser.getSelectedFile().getAbsolutePath();
                } else {
                    System.out.println("Dosya seçilmedi. Program sonlandırılıyor.");
                    return;
                }
            } catch (HeadlessException he) {
                // GUI olmayan ortamlarda konsol girişi kullan
                System.out.println("GUI mevcut değil. Dosya yolunu girin:");
                sourcePath = scanner.nextLine();
            }
        }

        try {

            readProgram(sourcePath);


            parseLabels();


            executeProgram();

        } catch (IOException e) {
            System.err.println("Dosya okuma hatasi: " + e.getMessage());
        }
    }

    private static void readProgram(String dosyaAdi) throws IOException {
        BufferedReader okuyucu = new BufferedReader(new FileReader(dosyaAdi));
        String satir;

        while ((satir = okuyucu.readLine()) != null) {
            satir = satir.trim();
            if (!satir.isEmpty()) {
                program.add(satir);
            }
        }
        okuyucu.close();
    }

    private static void parseLabels() {
        for (int i = 0; i < program.size(); i++) {
            String satir = program.get(i).trim();
            if (satir.contains(":")) {
                String etiketAdi = satir.substring(0, satir.indexOf(":")).trim();
                labels.put(etiketAdi, i);
            }
        }
    }

    private static void executeProgram() {
        programCounter = 0;

        while (programCounter < program.size()) {
            String satir = program.get(programCounter).trim();
            if (satir.isEmpty()) {
                programCounter++;
                continue;
            }


            if (satir.contains(":")) {
                satir = satir.substring(satir.indexOf(":") + 1).trim();
                if (satir.isEmpty()) {
                    programCounter++;
                    continue;
                }
            }

            //istediğim bir değeri yazabilirim
            if (satir.equals("SON")) {
                break;
            }

            executeLine(satir);
            programCounter++;
        }


        printRegisters();
    }

    private static void executeLine(String satir) {
        String[] parcalar = satir.split("\\s+");
        String komut = parcalar[0];

        switch (komut) {
            case "ATM":
                ATM(parcalar);
                break;
            case "TOP":
                TOP(parcalar);
                break;
            case "CRP":
                CRP(parcalar);
                break;
            case "CIK":
                CIK(parcalar);
                break;
            case "BOL":
                BOL(parcalar);
                break;
            case "VE":
                VE(parcalar);
                break;
            case "VEY":
                VEY(parcalar);
                break;
            case "D":
                D(parcalar);
                break;
            case "DEG":
                DEG(parcalar);
                break;
            case "DE":
                DE(parcalar);
                break;
            case "DED":
                DED(parcalar);
                break;
            case "DB":
                DB(parcalar);
                break;
            case "DBE":
                DBE(parcalar);
                break;
            case "DK":
                DK(parcalar);
                break;
            case "DKE":
                DKE(parcalar);
                break;
            case "OKU":
                OKU(parcalar);
                break;
            case "YAZ":
                YAZ(parcalar);
                break;
            default:
                System.err.println("Bilinmeyen komut: " + komut);
        }
    }


    private static void ATM(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int deger = getOperandValue(kaynak);
        setOperandValue(hedef, deger);
    }


    private static void TOP(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int hedefDeger = getOperandValue(hedef);
        int kaynakDeger = getOperandValue(kaynak);
        int sonuc = hedefDeger + kaynakDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void CRP(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int hedefDeger = getOperandValue(hedef);
        int kaynakDeger = getOperandValue(kaynak);
        int sonuc = hedefDeger * kaynakDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void CIK(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int hedefDeger = getOperandValue(hedef);
        int kaynakDeger = getOperandValue(kaynak);
        int sonuc = hedefDeger - kaynakDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void BOL(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int hedefDeger = getOperandValue(hedef);
        int kaynakDeger = getOperandValue(kaynak);
        int sonuc = hedefDeger / kaynakDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void VE(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int hedefDeger = getOperandValue(hedef);
        int kaynakDeger = getOperandValue(kaynak);
        int sonuc = hedefDeger & kaynakDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void VEY(String[] komutParcalari) {
        String hedef = komutParcalari[1];
        String kaynak = komutParcalari[2];

        int hedefDeger = getOperandValue(hedef);
        int kaynakDeger = getOperandValue(kaynak);
        int sonuc = hedefDeger | kaynakDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void DEG(String[] komutParcalari) {
        String hedef = komutParcalari[1];

        int hedefDeger = getOperandValue(hedef);
        int sonuc = ~hedefDeger;

        setOperandValue(hedef, sonuc);
        updateFlags(sonuc);
    }


    private static void D(String[] komutParcalari) {
        String etiket = komutParcalari[1];

        if (labels.containsKey(etiket)) {
            programCounter = labels.get(etiket) - 1;
        }
    }


    private static void DE(String[] komutParcalari) {
        if (SifirBayrak) {
            String etiket = komutParcalari[1];
            if (labels.containsKey(etiket)) {
                programCounter = labels.get(etiket) - 1;
            }
        }
    }


    private static void DED(String[] komutParcalari) {
        if (!SifirBayrak) {
            String etiket = komutParcalari[1];
            if (labels.containsKey(etiket)) {
                programCounter = labels.get(etiket) - 1;
            }
        }
    }


    private static void DB(String[] komutParcalari) {
        if (!SifirBayrak && !IsaretBayrak) {
            String etiket = komutParcalari[1];
            if (labels.containsKey(etiket)) {
                programCounter = labels.get(etiket) - 1;
            }
        }
    }


    private static void DBE(String[] komutParcalari) {
        if (!IsaretBayrak) {
            String etiket = komutParcalari[1];
            if (labels.containsKey(etiket)) {
                programCounter = labels.get(etiket) - 1;
            }
        }
    }


    private static void DK(String[] komutParcalari) {
        if (IsaretBayrak) {
            String etiket = komutParcalari[1];
            if (labels.containsKey(etiket)) {
                programCounter = labels.get(etiket) - 1;
            }
        }
    }


    private static void DKE(String[] komutParcalari) {
        if (IsaretBayrak || SifirBayrak) {
            String etiket = komutParcalari[1];
            if (labels.containsKey(etiket)) {
                programCounter = labels.get(etiket) - 1;
            }
        }
    }


    private static void OKU(String[] komutParcalari) {
        String hedef = komutParcalari[1];

        System.out.print("Deger girin: ");
        int deger = scanner.nextInt();

        setOperandValue(hedef, deger);
    }


    private static void YAZ(String[] komutParcalari) {
        String kaynak = komutParcalari[1];

        int deger = getOperandValue(kaynak);
        System.out.println(deger);
    }


    private static int getOperandValue(String operand) {
        operand = operand.trim();


        if (operand.startsWith("[") && operand.endsWith("]")) {
            String ic = operand.substring(1, operand.length() - 1);
            int adres;

            if (isRegister(ic)) {
                adres = getRegisterValue(ic);
            } else {
                adres = Integer.parseInt(ic);
            }

            return RAM[adres];
        }


        if (isRegister(operand)) {
            return getRegisterValue(operand);
        }


        return Integer.parseInt(operand);
    }


    private static void setOperandValue(String operand, int deger) {
        operand = operand.trim();


        if (operand.startsWith("[") && operand.endsWith("]")) {
            String ic = operand.substring(1, operand.length() - 1);
            int adres;

            if (isRegister(ic)) {
                adres = getRegisterValue(ic);
            } else {
                adres = Integer.parseInt(ic);
            }


            RAM[adres] = clampTo8Bit(deger);
            return;
        }


        setRegisterValue(operand, deger);
    }


    private static boolean isRegister(String operand) {
        return operand.equals("AX") || operand.equals("BX") ||
                operand.equals("CX") || operand.equals("DX");
    }


    private static int getRegisterValue(String registerAdi) {
        switch (registerAdi) {
            case "AX":
                return AX;
            case "BX":
                return BX;
            case "CX":
                return CX;
            case "DX":
                return DX;
            default:
                return 0;
        }
    }


    private static void setRegisterValue(String registerAdi, int deger) {

        deger = clampTo8Bit(deger);

        switch (registerAdi) {
            case "AX":
                AX = deger;
                break;
            case "BX":
                BX = deger;
                break;
            case "CX":
                CX = deger;
                break;
            case "DX":
                DX = deger;
                break;
        }
    }


    private static void updateFlags(int sonuc) {
        SifirBayrak = (sonuc == 0);
        IsaretBayrak = (sonuc < 0);
    }


    private static int clampTo8Bit(int value) {
        return (byte) value;
    }

    private static void printRegisters() {
        System.out.println("\n Register Dererleri");
        System.out.println("AX: " + AX);
        System.out.println("BX: " + BX);
        System.out.println("CX: " + CX);
        System.out.println("DX: " + DX);
    }
}