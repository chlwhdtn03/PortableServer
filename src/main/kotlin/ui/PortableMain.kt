package ui

import data.RouterObject
import file.FileManager
import javafx.collections.FXCollections
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.StageStyle
import tornadofx.*

class PortableMain : View("PortableServer") {
    private val routerController: RouterController by inject()
    private val objectController: ObjectController by inject()

    override val root = borderpane {

        top = menubar {
            menu("파일") {
                item("환경설정") {

                }
            }
        }

        center = hbox {
            // 라우터, 오브젝트
            vbox {
                listview (routerController.values) {
                    cellFormat {
                        val router = FileManager.loadRouter(it)
                        graphic = cache {
                            form {
                                fieldset {
                                    label(router.name) {

                                    }
                                    field("주소") {
                                        label(router.address)
                                    }
                                    field("Method Type") {
                                        label(router.type.name)
                                    }
                                    field("타겟 오브젝트") {
                                        label(router.target_data ?: "HTML")
                                        label(router.target_object?.name ?: "NULL")
                                        label(router.target_trigger?.name ?: "NULL")
                                    }
                                }

                            }
                        }
                    }
                }
                button("Add Router") {
                    action {
                        find<RouterEditor>().openWindow(stageStyle = StageStyle.UTILITY)
                    }
                }
            }

            vbox {
                listview(objectController.values)
                button("Add Object") {

                }
            }
        }

        right = vbox {
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

    class RouterController: Controller() {
        val values = FXCollections.observableArrayList(FileManager.loadRouters().map {
             it.split(".")[0]
        }.toMutableList())
    }
    class ObjectController: Controller() {
        val values = FXCollections.observableArrayList(FileManager.loadObjects().map {
            it.split(".")[0]
        }.toMutableList())
    }

}