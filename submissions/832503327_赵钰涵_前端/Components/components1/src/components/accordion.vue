<template>
  <div class="accordion">
    
    <div v-for="item in list" :key="item.index" class="item" 
       @mouseenter="show(item.index)" 
       @mouseleave="close()"
       :style="{flex:activeIndex===item.index?5:1}">

       <div class="item_image">
        <img :src="item.url">
        <!--渐变-->
        <div class="mask">
            <div :class="activeIndex===item.index?'title-left':'title-center'">
             {{ item.title }}
            </div>

            <div class="content" :style="{opacity:activeIndex===item.index?1:0}">
             {{ item.content }}
            </div>
        </div>

      
       </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 1. 数据列表
const list = ref([
  { index:1,title: '标题1', content: '内容11111111111111111111111111111111111111111',url:'/images/accordion_p1.jpg' },
  { index:2,title: '标题2', content: '内容22222222222222222222222222222222222222222',url:'/images/accordion_p2.jpg' },
  { index:3,title: '标题3', content: '内容33333333333333333333333333333333333333333',url:'/images/accordion_p3.jpg' },
  { index:4,title: '标题4', content: '内容44444444444444444444444444444444444444444',url:'/images/accordion_p4.jpg' },
  { index:5,title: '标题5', content: '内容55555555555555555555555555555555555555555',url:'/images/accordion_p5.jpg' },
])

const activeIndex = ref(-1)


function show(index){
  activeIndex.value = index
}
function close(){
  activeIndex.value = -1
}



</script>

<style scoped>
.accordion{
  width: 1200px;
  height: 400px;
  display: flex;
  justify-content: space-between;
  background-color: white;
}
.item{
  height: 370px;
  margin: 15px;
  overflow: hidden;
  display: block;
  background-color: aqua;
  border-radius: 12px;
  transition: flex 0.4s ease;
}
.item_image{
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.item_image img{
  height: 370px;
  width: 100%;
  object-fit: cover;/*不变形不压缩*/
  object-position: center;/*图片居中*/
  /*transform: scale(0.6);/*缩小到60%*/
}
.mask {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  /*height: 80px;
  background: linear-gradient(to top,rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.7) 30%, transparent 90%);*/
  height: 80px; 
  background: linear-gradient(to top, rgba(0,0,0,0.75), transparent);
  z-index: 1;
}

.title-center,
.title-left {
 position: absolute;
 font-size: 20px;
 font-weight: bold;
 color: aliceblue;
 transition: all 0.4s ease;

  /* 固定高度,只改 left 水平位置 */
  top: 20%;        /* 高度固定死，不再变化 */
  transform: none; /* 关掉垂直偏移 */
}
.title-center{
  left:50%;
  transform: translate(-50%);
  
}
.title-left{
  left: 20px;
}
.content{
  position: absolute;
  left: 20px;
  top: 50px;
  color: #eee;
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.4s ease;
  z-index: 2;
  white-space: nowrap;
}
</style>