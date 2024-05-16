<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  html,
  body,
  #app,
  #app > div {
    height: 100%;
  }
  .loading-overlay {
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    background-color: transparent;
  }
</style>
