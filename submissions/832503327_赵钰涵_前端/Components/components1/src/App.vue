<template>
  <div>
    <h1>在这里测试组件</h1>
    <ShowCard/>
    <div>
      <!-- 复用1：木棉卡片 -->
     <ShowCard1
       title="木棉"
       :content="mianianContent"
       show-text="显示木棉"
       hide-text="隐藏木棉"
     />

     <!-- 复用2：随便改个标题内容 -->
     <ShowCard1
       title="松树"
       :content="['松树是常青树','耐寒能力很强']"
       show-text="显示松树"
     />
    </div>
    <Accordion/>
  </div>

</template>

<script setup lang="ts">
import ShowCard from './components/showCard.vue'
import ShowCard1 from './components/showCard1.vue'
import Accordion from './components/accordion.vue'

const mianianContent = [
  '木棉（学名：Bombax ceiba L.）是锦葵科木棉属落叶大乔木，树皮灰白色，幼树的树干通常有圆锥状的粗刺；分枝平展。掌状复叶，小叶5-7片，长圆形至长圆状披针形，花单生枝顶叶腋，通常为红色，有时橙红色，直径约10厘米；萼杯状，长2-3厘米，外面无毛，内面密被淡黄色短绢毛，萼齿3-5，半圆形，花瓣肉质，倒卵状长圆形，蒴果长圆形，钝。分布于印度、斯里兰卡、中南半岛、马来西亚、印度尼西亚至菲律宾、澳大利亚北部和中国。在中国分布于亚热带省区。木棉生于海拔1400（-1700）米以下的干热河谷及稀树草原，也可生长在沟谷季雨林内',
  '花可入药，把新鲜的木棉花晒干后煮粥或煲汤 [20]，清热除湿，能治菌痢、肠炎、胃痛；根皮祛风湿、理跌打；树皮为滋补药，亦用于治痢疾和月经过多。果内绵毛可作枕、褥、救生圈等填充材料。种子油可作润滑油、制肥皂。木材轻软，可用作蒸笼、箱板、火柴梗、造纸等用。花大而美，树姿巍峨，可植为园庭观赏树，行道树'
]
</script>



<style scoped></style>
