package view;

import view.WeatherData;
import javax.swing.JFileChooser; // <-- Tambahkan ini
import javax.swing.filechooser.FileNameExtensionFilter; // <-- Dan ini untuk filter file
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon; // For displaying images
import java.awt.Image; // For scaling images
import java.util.Map;
import java.util.HashMap;

public class CekCuacaFrame extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private List<String> favoriteCities;
    
    private Map<String, Integer> searchCounts;
    
    private final WeatherAPIClient apiClient = new WeatherAPIClient();
    
    public CekCuacaFrame() {
        initComponents();
        initializeTable(); // New method to set up the table model
        initializeFavorites(); // New method to set up favorite cities
        
        searchCounts = new HashMap<>();
    }
    
    private void initializeTable() {
    // Column headers based on typical weather data (City, Temp, Description, Humidity)
    String[] columnNames = {"Kota", "Suhu (°C)", "Deskripsi", "Kelembaban (%)"};
    tableModel = new DefaultTableModel(columnNames, 0);
    jTable1.setModel(tableModel);
    }
    
    private void initializeFavorites() {
    favoriteCities = new ArrayList<>();
    // You can add some default cities if needed
    // favoriteCities.add("London");
    // favoriteCities.add("Tokyo");
    
    // Set the initial model for the JComboBox
    jComboBoxFavorit.setModel(new DefaultComboBoxModel<>(favoriteCities.toArray(new String[0])));
    jComboBoxFavorit.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jComboBoxFavoritItemStateChanged(evt);
        }
    });
    }
    
private void fetchAndDisplayWeather(String city) {
    // Jalankan API call di thread terpisah agar GUI tidak hang
    new Thread(() -> {
        // Ambil data menggunakan API Client
        // Pastikan Anda sudah mengimpor atau memiliki akses ke kelas WeatherAPIClient dan WeatherData
        WeatherData data = apiClient.fetchAndParseWeather(city);
        
        // Perbarui GUI di Event Dispatch Thread (Swing-friendly)
        java.awt.EventQueue.invokeLater(() -> {
            if (data != null) {
                // 1. Update Tabel
                updateTable(data.getCityName(), data.getTemperature(), data.getDescription(), data.getHumidity());

                // 2. Update Gambar Cuaca
                // Kirim HANYA kode ikon (e.g., "03n"). Penambahan URL dan .png dilakukan di method updateWeatherImage.
                updateWeatherImage(data.getWeatherIconCode());
                
                JOptionPane.showMessageDialog(this, "Cuaca untuk " + data.getCityName() + " berhasil dimuat!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Gagal mendapatkan atau memparsing data
                tableModel.setRowCount(0); 
                jLabelCuaca.setIcon(null);
                jLabelCuaca.setText("Data cuaca tidak ditemukan!");
                JOptionPane.showMessageDialog(this, "Gagal memuat data cuaca. Cek nama kota dan koneksi Anda.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }).start(); // Mulai thread baru
}
    private void updateTable(String city, double temp, String description, int humidity) {
    tableModel.setRowCount(0);
    tableModel.addRow(new Object[]{
        city,
        String.format("%.1f", temp), 
        description,
        humidity
    });
}
private void updateWeatherImage(String iconCode) {
    // URL dasar yang benar: https://openweathermap.org/img/wn/KODE_IKON@2x.png
    // iconCode seharusnya sudah berupa kode mentah (misalnya "03n").
    String iconUrlString = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
    
    try {
        // 1. Buat URL dari string
        java.net.URL imageUrl = new java.net.URL(iconUrlString);
        
        // 2. Ambil gambar dari URL
        ImageIcon icon = new ImageIcon(imageUrl);
        
        // Cek status pemuatan gambar untuk menangkap error HTTP atau masalah koneksi
        if (icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
             throw new Exception("Gagal memuat gambar dari URL: " + iconUrlString);
        }
        
        // 3. Skala gambar agar sesuai dengan ukuran JLabel (150x150)
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); 
        jLabelCuaca.setIcon(new ImageIcon(image));
        jLabelCuaca.setText(""); // Hapus teks jika gambar berhasil dimuat
        
    } catch (Exception e) {
        // Tangani jika terjadi kesalahan koneksi atau URL tidak valid
        jLabelCuaca.setText("Gambar Gagal Dimuat");
        jLabelCuaca.setIcon(null);
        System.err.println("Error memuat gambar dari web: " + e.getMessage());
        e.printStackTrace();
    }
}  
    private void addCityToFavorites(String city) {
    // Cek lagi untuk berjaga-jaga
    if (!favoriteCities.contains(city)) {
        favoriteCities.add(city);
        // Selalu update JComboBox model
        jComboBoxFavorit.setModel(new DefaultComboBoxModel<>(favoriteCities.toArray(new String[0])));
    }
}
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCariKota = new javax.swing.JTextField();
        jButtonCekCuaca = new javax.swing.JButton();
        jComboBoxFavorit = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonExport = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelCuaca = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("APLIKASI CEK CUACA");

        jButtonCekCuaca.setText("CEK CUACA");
        jButtonCekCuaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCekCuacaActionPerformed(evt);
            }
        });

        jComboBoxFavorit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxFavoritItemStateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtonExport.setText("EXPORT");
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        jButtonImport.setText("IMPORT");
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        jLabel2.setText("CARI KOTA");

        jLabel3.setText("FAVORIT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonImport)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(170, 170, 170))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCariKota, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFavorit, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCekCuaca))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelCuaca, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(41, 41, 41)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCariKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxFavorit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(jButtonCekCuaca))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabelCuaca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonExport)
                    .addComponent(jButtonImport)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCekCuacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCekCuacaActionPerformed
       String city = jTextFieldCariKota.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama kota.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Tambahkan Hitungan Pencarian
        String keyCity = city.toLowerCase();
        int currentCount = searchCounts.getOrDefault(keyCity, 0);
        int newCount = currentCount + 1;
        searchCounts.put(keyCity, newCount);
        
        // 2. Periksa apakah sudah mencapai ambang batas (3 kali)
        if (newCount >= 3 && !favoriteCities.contains(city)) {
            addCityToFavorites(city); 
            JOptionPane.showMessageDialog(this, city + " otomatis ditambahkan ke Favorit (Dicari " + newCount + " kali)!", 
                                          "Otomatis Favorit", JOptionPane.INFORMATION_MESSAGE);
            // Opsional: searchCounts.put(keyCity, 0); // Reset hitungan jika diinginkan
        }
        
        fetchAndDisplayWeather(city);
    }//GEN-LAST:event_jButtonCekCuacaActionPerformed

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
      // 1. Inisialisasi JFileChooser
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan Data Cuaca");
    
    // Tetapkan filter agar hanya menampilkan file .txt
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
    fileChooser.setFileFilter(filter);
    
    // Tetapkan nama file default
    fileChooser.setSelectedFile(new File("weather_data.txt"));

    // 2. Tampilkan dialog Save
    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        
        // Pastikan ekstensi .txt ditambahkan jika user lupa
        if (!filePath.toLowerCase().endsWith(".txt")) {
            fileToSave = new File(filePath + ".txt");
        }
        
        // Menggunakan try-with-resources untuk memastikan FileWriter ditutup
        try (FileWriter writer = new FileWriter(fileToSave)) {
            
            // Tulis Header (Nama Kolom)
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                writer.write(tableModel.getColumnName(i) + (i == tableModel.getColumnCount() - 1 ? "" : ","));
            }
            writer.write("\n");

            // Tulis Data Baris
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    Object value = tableModel.getValueAt(i, j);
                    writer.write((value != null ? value.toString() : "") + (j == tableModel.getColumnCount() - 1 ? "" : ","));
                }
                writer.write("\n");
            }

            // Tampilkan notifikasi sukses
            JOptionPane.showMessageDialog(this, "Data berhasil diexport ke " + fileToSave.getName(), "Sukses Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal export data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
        // 1. Inisialisasi JFileChooser
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih File Data Cuaca (.txt)");
    
    // Tetapkan filter agar hanya menampilkan file .txt
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
    fileChooser.setFileFilter(filter);
    
    // 2. Tampilkan dialog Open
    int userSelection = fileChooser.showOpenDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToOpen = fileChooser.getSelectedFile();
        
        // Pastikan file yang dipilih adalah .txt
        if (!fileToOpen.getName().toLowerCase().endsWith(".txt")) {
            JOptionPane.showMessageDialog(this, "Pilih hanya file .txt.", "Format Salah", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
            // Hapus data lama dari tabel
            tableModel.setRowCount(0);
            
            String line;
            boolean isHeader = true; 
            int expectedColumnCount = tableModel.getColumnCount();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; 

                if (isHeader) {
                    isHeader = false;
                    continue; // Lewati header
                }
                
                String[] data = line.split(",");
                
                if (data.length == expectedColumnCount) {
                    // Tambahkan baris baru ke tabel
                    tableModel.addRow(data);
                } else {
                    System.err.println("Peringatan: Baris dilewati karena format data tidak cocok: " + line);
                }
            }

            // Tampilkan notifikasi sukses
            JOptionPane.showMessageDialog(this, "Data berhasil diimport dari " + fileToOpen.getName(), "Sukses Import", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal import data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jComboBoxFavoritItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxFavoritItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
        String selectedCity = (String) jComboBoxFavorit.getSelectedItem();
        if (selectedCity != null && !selectedCity.isEmpty()) {
            fetchAndDisplayWeather(selectedCity);
        }
    }
    }//GEN-LAST:event_jComboBoxFavoritItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CekCuacaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CekCuacaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CekCuacaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CekCuacaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CekCuacaFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCekCuaca;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JComboBox<String> jComboBoxFavorit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelCuaca;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldCariKota;
    // End of variables declaration//GEN-END:variables
}
