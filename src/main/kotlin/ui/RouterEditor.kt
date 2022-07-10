package ui

import data.RouterMethod
import data.RouterObject
import file.FileManager
import io.vertx.ext.web.Router
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import tornadofx.*

class RouterEditor() : Fragment() {

    private val routerController: PortableMain.ObjectController by inject()
    private val methodController: PortableMain.MethodController by inject()

    var nameField : TextField by singleAssign()
    var addressField : TextField by singleAssign()
    var methodSelector : ComboBox<RouterMethod> by singleAssign()
    var targetSelector : ComboBox<String> by singleAssign()


    constructor(router: RouterObject) : this() {
        nameField.text = router.name
        addressField.text = router.address

        targetSelector.bindSelected(SimpleStringProperty(router.target_object?.name))
    }

    override val root = vbox {
        form {
            fieldset("기본 정보") {
                field("라우터 별명") {
                    textfield {
                        nameField = this
                    }
                }
                field("라우터 주소") {
                    textfield {
                        addressField = this
                    }
                }
                field("라우터 타입") {
                    combobox<RouterMethod> {
                        items = methodController.values
                        methodSelector = this
                    }
                }
            }
            fieldset("세부") {
                field("타겟 오브젝트") {
                    combobox<String> {
                        items = routerController.values.plus("타겟 없음").toObservable()
                        targetSelector = this
                    }
                }
            }
        }
    }

}

class RouterModel(router: RouterObject) : ItemViewModel<RouterObject>(router) {
    val routerName = bind(RouterObject::name)
    val routerAddress = bind(RouterObject::address)
    val routerType = bind(RouterObject::type)
    val routerTargetObject = bind(RouterObject::target_object)
    val routerTargetTrigger = bind(RouterObject::target_trigger)
    val routerTargetData = bind(RouterObject::target_data)
}