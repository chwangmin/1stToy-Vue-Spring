<template>
  <div id="app">
    <ToolBar />
    <ListItem />
  </div>
</template>

<script>
import ToolBar from './components/ToolBar.vue'
import ListItem from './components/ListItem.vue'

export default {
  name: 'app',
  components: {
    ToolBar,
    ListItem
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
