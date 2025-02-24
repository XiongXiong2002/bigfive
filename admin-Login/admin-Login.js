import {validate} from "../apiHandle/api.js"; 

const getRecords = Vue.createApp({
    data() {
        return {
            username:"",
            password:"",


        };
    },
    methods:{
       async login(){
            if (!this.username || !this.password) {
                alert("usernmae or password can't be empty!")
                this.username ="";
                this.password ="";
                return;
            }
            const InputAdmin = {
                userName:this.username,
                passWord:this.password
            }

            let response = await validate(InputAdmin);
            if(response.error||!response){
                alert("error to validate ,please try again later")
                this.username ="";
                this.password ="";
                return;
                
            }else{
                sessionStorage.setItem("userName",this.username)
                sessionStorage.setItem("passWord",this.password)

                
                window.location.replace("../admin-manage/admin-manage.html")
            }
        },
        backToMain(){
            window.location.replace("../welcomePage/welcome.html")
        }
       
    }
});
getRecords.mount(".app")