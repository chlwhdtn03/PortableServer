package data

enum class TriggerType(korean: String) {
    ADD_DATA("데이터 추가"), REMOVE_DATA("데이터 삭제"),
    CHECK_DATA_BY_PRIMARY_KEY("데이터 검증"), MODIFY_DATA_BY_PRIMARY_KEY("데이터 수정"),
    GET_DATA("전체 데이터 받기")

}