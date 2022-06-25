package ui

import tornadofx.*

class PortableMainView : View("PortableServer") {
    override val root = borderpane {
        top = menubar {
            menu("파일") {
                item("환경설정") {

                }
            }
        }

        center = hbox {
            // 라우터, 오브젝트
        }

        right = hbox {
            // 방문자 목록
        }

        bottom = vbox {
            textarea {
                isEditable = false
                tooltip("Console Ouptut")
            }
            textfield {

            }
        }
    }
}
