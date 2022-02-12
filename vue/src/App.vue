<template>
  <div id="app">
    
      <navbar/>
      <!-- <router-link v-bind:to="{ name: 'home' }">Home</router-link>&nbsp;|&nbsp; -->
      <!-- <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link> -->
    <router-view /> 
  </div>
</template>
<script>
import Navbar from "./components/NavBar";
export default {
  name: 'app',
  components: {
    Navbar
  }
}
</script>
<style>

html,body{
  height:100%;
   font-family: "Dosis", sans-serif;
  
}
body{
align-content: center;


  

}

</style>
