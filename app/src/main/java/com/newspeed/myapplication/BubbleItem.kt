package com.newspeed.myapplication

data class BubbleItem(
    val keyword: String,
    val sum_com: Int,
    val cid: String
) {
    companion object {
        // HotTopicResponse 리스트를 BubbleItem 리스트로 변환하는 메서드
        fun createFromHotTopics(hotTopics: List<HotTopicResponse>): List<BubbleItem> {
            return hotTopics.map { hotTopic ->
                BubbleItem(
                    keyword = hotTopic.keyword,
                    sum_com = hotTopic.sumCom,
                    cid = hotTopic.cid
                )
            }
        }
        // BubblesResponse 리스트를 BubbleItem 리스트로 변환하는 메서드
        fun createFromBubbles(bubbles: List<BubbleResponse>): List<BubbleItem> {
            return bubbles.map { bubble ->
                BubbleItem(
                    keyword = bubble.keyword,
                    sum_com = bubble.sumCom,
                    cid = bubble.cid
                )
            }
        }

        // 샘플 데이터 생성
        fun createSampleData(): List<BubbleItem> {
            return listOf(
                BubbleItem("뉴스제목 1", 3, "1"),
                BubbleItem("뉴스제목 2", 9, "2"),
                BubbleItem("뉴스제목 3", 5, "3"),
                BubbleItem("뉴스제목 4", 8, "4"),
                BubbleItem("뉴스제목 5", 6, "5"),
                BubbleItem("뉴스제목 6", 2, "6"),
                BubbleItem("뉴스제목 7", 7, "7"),
                BubbleItem("뉴스제목 8", 4, "8"),
                BubbleItem("뉴스제목 9", 1, "9"),
                BubbleItem("뉴스제목 10", 10, "10")
            )
        }
    }
}