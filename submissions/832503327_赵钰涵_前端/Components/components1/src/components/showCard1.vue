<template>
  <div class="box">
    <button @click="showCard" class="showBtn">
      {{ btnText }}
    </button>
    <div class="card" v-if="show">
      <div class="cardHead" :style="{animationDelay:'0s'}">
        {{ title }}
      </div>

      <div class="cardBody" :style="{animationDelay:'0.2s'}">
        <p v-for="(item, index) in content" :key="index">{{ item }}</p>
      </div>
      <div class="cardFoot" :style="{animationDelay:'0.4s'}">
        <button class="showBtn" @click="showCard">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps } from 'vue'

// 父组件可传的参数 ← 这是核心修改点
const props = defineProps({
  // 卡片标题
  title: {
    type: String,
    default: '默认标题'
  },
  // 卡片内容（数组，一段一个p）
  content: {
    type: Array,
    default: () => ['默认内容']
  },
  // 按钮显示文字
  showText: {
    type: String,
    default: '显示'
  },
  // 按钮隐藏文字
  hideText: {
    type: String,
    default: '隐藏'
  }
})

const btnText = ref(props.showText)
const show = ref(false)

function showCard(){
  if(btnText.value === props.showText){
    btnText.value = props.hideText
    show.value = true
  }else{
    btnText.value = props.showText
    show.value = false
  }
}
</script>

<style scoped>
.box{
  width: 600px;
  height: 600px;
  background-color: white;
}
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
.card{
  width: 380px;
  height:400px;
  display: flex;
  flex-direction: column;
  padding: 24px;
  margin: 20px 0;
  background: #eaeded;
  border-radius: 16px;
  box-shadow: 0 8px 25px rgba(0, 80, 80, 0.08);
  box-sizing: border-box;
  overflow: hidden;
}
.cardBody {
  flex: 1;
  overflow-y: auto;
  padding-right: 8px;
  font-size: 15px;
  line-height: 1.7;
  color: #444;
  text-align: justify;
  text-indent:2em;
  margin-bottom: 24px;
}
.cardHead {
  font-size: 24px;
  font-weight: bold;
  color: #222;
  text-align: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}
.cardFoot {
  text-align: center;
}
.card {
  animation: scaleIn 0.5s ease forwards;
}
@keyframes scaleIn {
  from { opacity: 0; transform: scale(0.8); }
  to { opacity: 1; transform: scale(1); }
}
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