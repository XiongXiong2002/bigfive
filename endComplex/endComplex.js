function getResult() {
    if (!sessionStorage.getItem("score")) {
        window.location.replace("../welcomePage/Welcome.html");
        return;
    }
    console.log(sessionStorage.getItem("result"))

    let rawData = sessionStorage.getItem("result");
    if (!rawData) {
        console.error("❌ sessionStorage 里没有 result 数据");
        return;
    }

    try {
        let newData = rawData.replace(/```json\s*([\s\S]*?)\s*```/g, "$1").trim();
        let jsonObject = JSON.parse(newData); // **💡 确保解析 JSON 成对象**
        
        sessionStorage.setItem("parsedResult", JSON.stringify(jsonObject)); // **存入 JSON 字符串**
        console.log("✅ 解析成功并存入 sessionStorage:", jsonObject);
    } catch (error) {
        alert("❌JSON 解析失败:", error);
    }
    let scoreRecord = JSON.parse(sessionStorage.getItem("score"))
    let opennessTotal = 0;
    let conscientiousnessTotal = 0;
    let extraversionTotal = 0;
    let agreeablenessTotal = 0;
    let neuroticismTotal = 0;
    //array.reduce((累加值, 当前元素) => { 计算逻辑 }, 初始值);
    for (let category of scoreRecord) {
        let totalScore = Object.values(category.subcategories).reduce((sum, value) => sum + value, 0);
    
        switch (category.personality) {
            case "openness":
                opennessTotal = totalScore;
                break;
            case "conscientiousness":
                conscientiousnessTotal = totalScore;
                break;
            case "extraversion":
                extraversionTotal = totalScore;
                break;
            case "agreeableness":
                agreeablenessTotal = totalScore;
                break;
            case "neuroticism":
                neuroticismTotal = totalScore;
                break;
        }
    }
    window.total = opennessTotal +conscientiousnessTotal +extraversionTotal +agreeablenessTotal +neuroticismTotal;
    window.result = [
        { personality: "Openness", score: opennessTotal },
        { personality: "Conscientiousness", score: conscientiousnessTotal },
        { personality: "Extraversion", score: extraversionTotal },
        { personality: "Agreeableness", score: agreeablenessTotal },
        { personality: "Neuroticism", score: neuroticismTotal }
    ];
}

getResult()

const getRecords = Vue.createApp({
    data() {
        return {
            results:JSON.parse(sessionStorage.getItem("parsedResult")),
            categoryScores:window.result
        };
    },
    methods:{
        goBack(){
            window.location.assign("../welcomePage/welcome.html")
        }
       
    }
});
getRecords.mount("#app");
