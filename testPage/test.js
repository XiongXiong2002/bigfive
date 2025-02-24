import { getResult,getComplexResult} from "../apiHandle/api.js"; 

async function getQuestions() {
    window.questionSet = JSON.parse(sessionStorage.getItem("testQuestion"));
    if(!sessionStorage.getItem("score")){
        window.location.replace("../welcomePage/Welcome.html")
    }

    let sendyet = sessionStorage.getItem("haveSendYet") === "true"
    if(sendyet){
        document.querySelector(".loading-overlay").classList.add("show");
        try {
            if(window.questionSet.length <= 25) {
                const herfPage = "../endSimple/endSimple.html";
                const response = await getResult(JSON.parse(sessionStorage.getItem("score")));
                if (response.error) {
                    alert("Get Result Failed: " + ("Fail to fecth ,please try again"|| "Unknown error"))
                    location.reload();
                    return
                }
    
                // ✅ API 成功，存储结果并跳转
                sessionStorage.setItem("result", JSON.stringify(response));
                window.location.replace(herfPage);
            }else{
                const response = await getComplexResult(JSON.parse(sessionStorage.getItem("score")));
                const herfPage = "../endComplex/endComplex.html";
                if (response.error) {
                    alert("Get Result Failed: " + ("Fail to fecth ,please try again"|| "Unknown error"))
                    location.reload();
                    return
                }
                sessionStorage.setItem("result", response);
                window.location.replace(herfPage);

            }
            


        } catch (error) {
            console.error("获取测试结果失败:", error.message);
            alert("Get Result Failed: " + (error.message || "Unknown error"));
            location.reload();
            return
        }
        
    }
}

const stepCount = Vue.createApp({
    data() {
        return {
            currentStep: parseInt(sessionStorage.getItem("currentStep")),
            allStep: JSON.parse(sessionStorage.getItem("testQuestion")),
            currentQuestion: JSON.parse(sessionStorage.getItem("testQuestion"))[parseInt(sessionStorage.getItem("currentStep"))],
            options: ["Strongly agree", "Agree", "Neutral", "Disagree", "Strongly disagree"],
            score: JSON.parse(sessionStorage.getItem("score")),
            isAnimating: false,
            answers: JSON.parse(sessionStorage.getItem("answer"))
        };
    },
    methods: {
        async Choose(choice) {
            if (this.isAnimating) return; // 阻止重复点击
            this.isAnimating = true;
        
            // 记录当前选择
            this.answers[this.currentStep] = choice;
            sessionStorage.setItem("answer", JSON.stringify(this.answers));
        
            // 更新得分
            this.updateScore(choice);
        
            // 处理下一步逻辑
            if (this.allStep[this.currentStep + 1] != null) {
                // 还有下一题，切换问题
                const questionPart = document.querySelector(".QuestionPart");
                questionPart.classList.add("changing");
        
                setTimeout(() => {
                    this.currentStep++;
                    sessionStorage.setItem("currentStep", this.currentStep);
                    this.currentQuestion = this.allStep[this.currentStep];
        
                    questionPart.classList.remove("changing");
                    this.isAnimating = false;
                }, 300);
            } else {
                // **最后一题，提交数据**
                sessionStorage.setItem("haveSendYet",true)
                document.querySelector(".loading-overlay").classList.add("show"); // 显示加载动画
        
                try {
                    if(this.allStep.length <= 25) {
                        const herfPage = "../endSimple/endSimple.html";
                        const response = await getResult(this.score);
                        if (response.error) {
                            alert("Get Result Failed: " + ("Fail to fecth ,please try again"|| "Unknown error"))
                            location.reload();
                            return
                        }
            
                        // ✅ API 成功，存储结果并跳转
                        sessionStorage.setItem("result", JSON.stringify(response));
                        window.location.replace(herfPage);
                    }else{
                        const response = await getComplexResult(this.score);
                        const herfPage = "../endComplex/endComplex.html";
                        if (response.error||!response) {
                            alert("Get Result Failed: " + ("Fail to fecth ,please try again"|| "Unknown error"))
                            location.reload();
                            return
                        }
                        sessionStorage.setItem("result", response);
                        window.location.replace(herfPage);
                       

                    }
                    
        

                } catch (error) {
                    console.error("获取测试结果失败:", error.message);
                    alert("Get Result Failed: " + (error.message || "Unknown error"));
                    location.reload();
                    return;
                }
            }
        },
        


        goBack() {
            if (this.currentStep > 0) {
                // 1️ **回滚当前问题的分数**
                this.rollbackScore(this.currentStep);
        
                // 2️ **回滚上一个问题的分数**
                this.rollbackScore(this.currentStep - 1);
        
                // 3️ **移动到上一个问题**
                this.currentStep--;
                sessionStorage.setItem("currentStep", this.currentStep);
                this.currentQuestion = this.allStep[this.currentStep];
            }
        },
        
        rollbackScore(step) {
            // **检查 step 是否有效**
            if (step < 0 || this.answers[step] === undefined) return;
        
            // **获取该 step 对应的问题的 category 和 subcategory**
            const categoryName = this.allStep[step].category;
            const subcategoryName = this.allStep[step].subcategory;
        
            // **找到 category 并检查 subcategories 是否存在**
            const category = this.score.find(c => c.personality === categoryName);
            if (category && category.subcategories && category.subcategories[subcategoryName] !== undefined) {
                // **获取当前问题的选项，并计算得分**
                const choice = this.answers[step];
                const scoreValue = this.calculateScore(this.options[choice]);
        
                // **扣除分数**
                category.subcategories[subcategoryName] -= scoreValue;
            }
        
            // **移除当前问题的答案**
            delete this.answers[step];
        
            // **更新 sessionStorage**
            sessionStorage.setItem("score", JSON.stringify(this.score));
            sessionStorage.setItem("answer", JSON.stringify(this.answers));
        },
        
        updateScore(choice) {
            // 计算当前选择的得分
            const scoreValue = this.calculateScore(this.options[choice]);

            // 获取当前问题的 category 和 subcategory
            const categoryName = this.currentQuestion.category;
            const subcategoryName = this.currentQuestion.subcategory;
            // 找到对应的 category 和 subcategory，并更新得分
            for (const category of this.score) {
                if (category.personality === categoryName) {
                    if (category.subcategories[subcategoryName] !== undefined) {
                        category.subcategories[subcategoryName] += scoreValue;
                    }
                    break; // 找到后退出循环
                }
            }

            // 将更新后的得分保存回 sessionStorage
            sessionStorage.setItem("score", JSON.stringify(this.score));
        },
        calculateScore(choice) {
            // 根据选项计算得分
            switch (choice) {
                case "Strongly agree": return 5;
                case "Agree": return 4;
                case "Neutral": return 3;
                case "Disagree": return 2;
                case "Strongly disagree": return 1;
                default: return 0;
            }
        }
    }
});



getQuestions();
stepCount.mount(".QuestionContainer");