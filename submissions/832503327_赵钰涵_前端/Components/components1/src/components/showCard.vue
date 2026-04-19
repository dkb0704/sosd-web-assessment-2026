<template>
  <div class="box">
    <button @click="showCard" class="showBtn">
      {{ state }}
    </button>
    <div class="card" v-if="show">
      <div class="cardHead" :style="{animationDelay:'0s'}">木棉</div>

      <div class="cardBody" :style="{animationDelay:'0.2s'}">
        
          <p>木棉（学名：Bombax ceiba L.）是锦葵科木棉属落叶大乔木，树皮灰白色，幼树的树干通常有圆锥状的粗刺；分枝平展。掌状复叶，小叶5-7片，长圆形至长圆状披针形，花单生枝顶叶腋，通常为红色，有时橙红色，直径约10厘米；萼杯状，长2-3厘米，外面无毛，内面密被淡黄色短绢毛，萼齿3-5，半圆形，花瓣肉质，倒卵状长圆形，蒴果长圆形，钝。分布于印度、斯里兰卡、中南半岛、马来西亚、印度尼西亚至菲律宾、澳大利亚北部和中国。在中国分布于亚热带省区。木棉生于海拔1400（-1700）米以下的干热河谷及稀树草原，也可生长在沟谷季雨林内</p>
          <p>花可入药，把新鲜的木棉花晒干后煮粥或煲汤 [20]，清热除湿，能治菌痢、肠炎、胃痛；根皮祛风湿、理跌打；树皮为滋补药，亦用于治痢疾和月经过多。果内绵毛可作枕、褥、救生圈等填充材料。种子油可作润滑油、制肥皂。木材轻软，可用作蒸笼、箱板、火柴梗、造纸等用。花大而美，树姿巍峨，可植为园庭观赏树，行道树。</p>
      </div>
      <div class="cardFoot" :style="{animationDelay:'0.4s'}">
        <button class="showBtn" @click="showCard">关闭</button>
      </div>
      
    </div>
  </div>
</template>

<script setup lang="ts">
  import {ref} from 'vue'

  const state = ref('显示')
  const show = ref(false)

  function showCard(){
    if(state.value=='显示'){
      state.value='隐藏'
      show.value=true
    }else{
      state.value='显示'
      show.value=false
    }

  }
  
</script>

<style scoped>
.box{
  width: 600px;
  height: 600px;
  background-color: white;
}
/*按钮*/
.showBtn {
  padding: 8px 16px;
  background-color: #b3b4b5;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
}
.showBtn:hover {
  background-color:#9b9a9a;
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(74, 135, 137, 0.2);
}
.showBtn:active {
  transform: translateY(0);
}

/*卡片*/
.card{
  width: 380px;
  height:400px;
  display: flex;
  flex-direction: column;/*让内部自己分区*/
  padding: 24px;
  margin: 20px 0;
  background: #eaeded;
  border-radius: 16px;
  box-shadow: 0 8px 25px rgba(0, 80, 80, 0.08);
  box-sizing: border-box;
  overflow: hidden;
}

.cardBody {
  flex: 1;  /*让内容占满剩余的区域*/
  overflow-y: auto;  /*滚动*/
  padding-right: 8px;
  font-size: 15px;
  line-height: 1.7;
  color: #444;
  text-align: justify;
  /*每段开头空两格*/
  text-indent:2em;
  margin-bottom: 24px;
}


/* 卡片标题 */
.cardHead {
  font-size: 24px;
  font-weight: bold;
  color: #222;
  text-align: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}



/* 卡片底部 */
.cardFoot {
  text-align: center;
}


/*加入卡片出场的动画*/
.card {
  animation: scaleIn 0.5s ease forwards;
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/*给卡片里的内容也加点动画*/
.cardHead,
.cardBody,
.cardFoot {
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

@keyframes fadeInUp {
  from { opacity:0; transform: translateY(20px); }
  to   { opacity:1; transform: translateY(0); }
}
</style>