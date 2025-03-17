import { addElement, getAll, deleteQuestion } from "../apiHandle/api.js";

async function getInfo() {
    window.userInfo = {
        userName: sessionStorage.getItem("userName"),
        passWord: sessionStorage.getItem("passWord")
    };

    // ✅ 如果 `userInfo` 为空，跳回登录页面
    if (!window.userInfo.userName || !window.userInfo.passWord) {
        alert("Session expired. Please log in again.");
        window.location.replace("../admin-Login/admin-Login.html"); 
    }
}
getInfo();

const infoPresent = Vue.createApp({
    data() {
        return {
            userInfo: window.userInfo,
            questions: window.response,
            currentAdding: [],
            newQuestionsJson: "",
            logoutTimer: null  // ✅ 添加一个定时器变量
        };
    },
    async mounted() {
        try {
            this.questions = await getAll(this.userInfo);
            console.log("Questions loaded:", this.questions);
            console.log("完成拉");
        } catch (error) {
            console.error("Error loading questions:", error);
        }

        // ✅ 启动自动登出计时器
        this.resetLogoutTimer();
        window.addEventListener("mousemove", this.resetLogoutTimer);
        window.addEventListener("keydown", this.resetLogoutTimer);
        window.addEventListener("click", this.resetLogoutTimer);
    },
    beforeUnmount() {
        // ✅ 移除监听事件，防止内存泄漏
        window.removeEventListener("mousemove", this.resetLogoutTimer);
        window.removeEventListener("keydown", this.resetLogoutTimer);
        window.removeEventListener("click", this.resetLogoutTimer);
    },
    methods: {
        resetLogoutTimer() {
            clearTimeout(this.logoutTimer);
            this.logoutTimer = setTimeout(() => {
                this.logout();
            },  10*60*1000); // 10分钟（单位：毫秒）
        },
        logout() {
            alert("You have been logged out due to inactivity.");
            sessionStorage.clear();
            window.location.replace("../admin-Login/admin-Login.html"); 
        },

        handleFileUpload(event) {
            const file = event.target.files[0];
            if (!file) return;
            const reader = new FileReader();
            reader.readAsText(file);
            reader.onload = () => {
                try {
                    const jsonData = JSON.parse(reader.result);
                    if (!Array.isArray(jsonData)) {
                        alert("Invalid type of file, please check.");
                        return;
                    }
                    this.currentAdding = [...this.currentAdding, ...jsonData];
                    console.log("✅ JSON 解析成功:");
                } catch (error) {
                    console.error("❌ JSON 解析失败:", error);
                    alert("Invalid file format.");
                }
            };
        },

        async handleTextInput() {
            if (this.newQuestionsJson.trim() === "" && this.currentAdding.length === 0) {
                alert("Input can't be empty");
                return;
            }
        
            let currentinput = [];
            if (this.newQuestionsJson) {
                try {
                    currentinput = JSON.parse(this.newQuestionsJson);
                    
                    if (!Array.isArray(currentinput)) {
                        alert("Invalid input format, expected an array.");
                        this.currentAdding =[];
                    this.newQuestionsJson = "";
                        return;
                    }
        
                    // **获取所有已存在的 question content**
                    const existingContents = new Set(this.questions.map(q => q.content));
        
                    // **过滤掉重复的内容**
                    currentinput = currentinput.filter(q => !existingContents.has(q.content));
        
                    if (currentinput.length === 0) {
                        alert("All input questions already exist.");
                                            this.currentAdding =[];
                    this.newQuestionsJson = "";
                        return;
                    }
        
                } catch (error) {
                    alert("Invalid input format.");
                                        this.currentAdding =[];
                    this.newQuestionsJson = "";
                    return;
                }
            }
        
            // **合并 `currentAdding` 和 `currentinput`，同时去重**
            const mergedQuestions = [...this.currentAdding, ...currentinput];
        
            // **检查格式是否正确**
            for (let js of mergedQuestions) {
                if (!js.content || !js.category || !js.subcategory || Object.keys(js).length !== 3) {
                    alert("Invalid input format.");
                                        this.currentAdding =[];
                    this.newQuestionsJson = "";
                    return;
                }
            }
        
            // **更新 `currentAdding`，提交后清空 `newQuestionsJson`**
     
            this.userInfo.question = mergedQuestions;
        
            const response2 = await addElement(this.userInfo);
            if (response2.error || !response2) {
                alert("Add question failed, please try again.");
                console.log(this.currentAdding);
                    this.currentAdding =[];
                    this.newQuestionsJson = "";
            } else {
                try {
                    let response3 = await getAll(this.userInfo);
                    if (response3.error || !response3) {
                        alert("Fail to get questions, please check server.");
                        return;
                    }
                    this.questions = response3;
        
                    this.currentAdding =[];
                    console.log("完成拉");
                    this.newQuestionsJson = "";
                } catch (error) {
                    console.error("Error loading questions:", error);
                    alert("Error loading questions: " + error);
                }
            }
            this.currentAdding =[];
                    this.newQuestionsJson = "";
        },
        async deleteByIndex(index) {
            var commit ={
                id:this.questions[index].id,
                subCategory:this.questions[index].subcategory
            }
            try {
                const response = await deleteQuestion(commit);
                if (response.error || !response) {
                    if(!response){
                        alert("Can not remove question when Subcategory questions are less than 10! please add more then try again");
                    }else{
                        alert("Fail to delete, the reason is : "+response.error );
                    }
                    
                    return;
                } else {
                    try {
                        let response3 = await getAll(this.userInfo);
                        if (response3.error || !response3) {
                            alert("Fail to get questions, please check server.");
                            return;
                        }
                        this.questions = response3;
                    } catch (error) {
                        console.error("Error loading questions:", error);
                        alert("Error loading questions: " + error);
                    }
                }
            } catch (error) {
                console.log(error);
                alert("Fail to delete, please check server.");
            }
        }
        
        ,
        backToMain(){
            window.removeEventListener("mousemove", this.resetLogoutTimer);
            window.removeEventListener("keydown", this.resetLogoutTimer);
            window.removeEventListener("click", this.resetLogoutTimer);
            window.location.replace("../Welcome.html")
        }

    }
});

infoPresent.mount(".app");
