function setUpResult(){
    if(!sessionStorage.getItem("score")){
        window.location.replace("../welcomePage/Welcome.html")
    }
    console.log(JSON.parse(sessionStorage.getItem("result")))
    window.resultComment = JSON.parse(sessionStorage.getItem("result"))
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
    
    console.log(window.ResultComment)

    
}

setUpResult();


const getRecords = Vue.createApp({
    data() {
        return {
            result:window.result,
            comment: window.resultComment,
            total: window.total
        };
    }
});

// 挂载 Vue 应用

getRecords.mount(".resultContainer");


