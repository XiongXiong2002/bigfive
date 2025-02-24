import { getQuestions } from "../apiHandle/api.js"; 

const swiper = new Swiper('.swiper-container', {
    loop: true, // 循环播放
    autoplay: {
        delay: 5000, // 每 3 秒切换
        disableOnInteraction: false, // 用户操作后继续自动播放
    },
    pagination: {
        el: '.swiper-pagination',
        clickable: true, // 分页指示器可点击
        type: 'bullets', // 设置分页器为小圆点
    },
    navigation: {
        nextEl: '.swiper-button-next', // 下一页按钮
        prevEl: '.swiper-button-prev', // 上一页按钮
    },
});

// 处理跳转到下个页面
async function testChosen(header) {
    document.querySelector(".loading-overlay").classList.add("show"); // 显示加载动画

    try {
        const response = await getQuestions(header.classList[0]); // 调用后端 API

        if (!response || response.error) {
            alert("Server request error ,please try again later")
            location.reload();
            return;
        }

        // API 成功，存储数据
        sessionStorage.setItem("testQuestion", JSON.stringify(response));
        sessionStorage.setItem("currentStep", 0);
        sessionStorage.setItem("answer", JSON.stringify([]));

        // 初始化 `score`
        const initialScores = [
            { personality: "openness", subcategories: { imagination: 0, artistic_interests: 0, emotional_depth: 0, adventurousness: 0, intellectual_curiosity: 0, openness_to_values: 0 } },
            { personality: "conscientiousness", subcategories: { self_discipline: 0, orderliness: 0, responsibility: 0, achievement_striving: 0, cautiousness: 0, time_management: 0, attention_to_detail: 0 } },
            { personality: "extraversion", subcategories: { sociability: 0, assertiveness: 0, energy_level: 0, adventurousness: 0, optimism: 0 } },
            { personality: "agreeableness", subcategories: { trust: 0, morality: 0, altruism: 0, cooperation: 0, modesty: 0, sympathy: 0 } },
            { personality: "neuroticism", subcategories: { anxiety: 0, anger: 0, depression: 0, self_consciousness: 0, vulnerability: 0, stress_tolerance: 0 } }
        ];

        sessionStorage.setItem("score", JSON.stringify(initialScores));
        sessionStorage.setItem("haveSendYet",false);

        // API 成功，跳转到测试页面
        document.querySelector(".loading-overlay").classList.remove("show");
        window.location.replace("../testPage/test.html");

    } catch (error) {
        console.error("API Failed:", error.message);
        document.querySelector(".loading-overlay").classList.remove("show"); // 关闭加载动画
        alert("出错了：" + error.message); // 让用户看到错误信息
    }
}


function OpenMenu1(header){
    const content=document.querySelector(".content1")

    if(window.getComputedStyle(content).maxHeight=== "0px"){
        content.style.maxHeight = content.scrollHeight + "px";
        header.style.transform = "rotate(45deg)"
    }else{
        content.style.maxHeight ="0px";
        header.style.transform = "rotate(0deg)"
    }
    
}

function OpenMenu2(header){
    const content=document.querySelector(".content2")
    if(window.getComputedStyle(content).maxHeight=== "0px"){
        content.style.maxHeight = content.scrollHeight + "px";
        header.style.transform = "rotate(45deg)"
    }else{
        content.style.maxHeight ="0px";
        header.style.transform = "rotate(0deg)"
    }
}

function OpenMenu3(header){
    const content=document.querySelector(".content3")
    if(window.getComputedStyle(content).maxHeight=== "0px"){
        content.style.maxHeight = content.scrollHeight + "px";
        header.style.transform = "rotate(45deg)"
    }else{
        content.style.maxHeight ="0px";
        header.style.transform = "rotate(0deg)"
    }
}

function getToManager(header){
    window.location.replace("../admin-Login/admin-Login.html");
}


window.testChosen = testChosen;
window.OpenMenu1 =OpenMenu1;
window.OpenMenu2 = OpenMenu2;
window.OpenMenu3 =OpenMenu3;
window.getToManager = getToManager;

