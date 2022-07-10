package ui.setting

import data.Option
import data.PointerBoolean
import ui.component.PortableToggle
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JScrollPane
import javax.swing.filechooser.FileNameExtensionFilter

class SSLPanel : OptionPanel() {
    private val isSSL: PointerBoolean = PointerBoolean(false)
    private val filechooser: JFileChooser = JFileChooser(".")
    init {

        val toggleSSL = PortableToggle(isSSL)

        val findKeystore = JButton("찾기")
        val findTruststore = JButton("찾기")

        add(JScrollPane(getSettingPanel(
            Option("SSL 사용", toggleSSL),
            Option("KeyStore 위치", findKeystore),
            Option("TrustStore 위치", findTruststore)
        )))

        filechooser.fileFilter = FileNameExtensionFilter("산업 표준 pkcs12사용을 권장합니다", "pkcs12")
        filechooser.isMultiSelectionEnabled = false;

        findKeystore.addActionListener {
            if(filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                println(filechooser.selectedFile.path)
            }
        }

        findTruststore.addActionListener {
            if(filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                println(filechooser.selectedFile.path)
            }
        }

    }


}