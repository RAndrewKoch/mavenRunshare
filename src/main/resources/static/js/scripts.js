let navClosed = true;

function openNav(){
    if (navClosed===true){
      if (screen.width<800){
          document.getElementById("sideNav").style
          .width="100vw";
          document.getElementById("main").style
          .width="100vw";
          navClosed=false;
          } else {
          document.getElementById("sideNav").style.width ="25%";
          document.getElementById("main").style
          .width="75%";
          document.getElementById("main").style
          .marginLeft="25%";
          navClosed = false;
         }
    } else {
         closeNav();
    }
}

function closeNav() {
    document.getElementById("sideNav").style.width = "0";
    document.getElementById("main").style
    .marginLeft="0";
    document.getElementById("main").style.width="100%";
    navClosed= true;
}

function openMenu(menuId){
    let runnerMenu = document.getElementById(menuId);
    runnerMenu.style.transition = "all 1s";
    runnerMenu.removeAttribute("style");
}

function closeMenu(menuId){
    let runnerMenu = document.getElementById(menuId);
    console.log("closeRunners fired");
    runnerMenu.style.display = "none";
    ;
}
