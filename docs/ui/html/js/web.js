// 登录弹窗模块（使用IIFE创建闭包，避免污染全局空间）
const LoginModal = (() => {
    let intervalId = null;      // 轮询定时器ID
    let currentTicket = null;   // 当前二维码票据

    // DOM元素缓存
    const elements = {
        mask: document.querySelector('.login-mask'),    // 遮罩层
        qrImg: document.getElementById('qrCodeImg'),    // 二维码图片
        closeBtn: document.querySelector('.close-btn')  // 关闭按钮
    };

    // 初始化事件监听
    const initEvents = () => {
        // 关闭按钮点击事件
        elements.closeBtn.addEventListener('click', close);
        // 遮罩层点击关闭（事件委托）
        elements.mask.addEventListener('click', e => {
            if (e.target === elements.mask) close();
        });
        // ESC键关闭监听
        document.addEventListener('keydown', e => {
            if (e.key === 'Escape') close();
        });
    };

    // 显示登录弹窗
    const show = async () => {
        elements.mask.style.display = 'flex';  // 显示遮罩层
        try {
            // 请求二维码票据接口
            const response = await fetch('http://127.0.0.1:8080/api/v1/login/weixin_qrcode_ticket');
            const data = await response.json();

            if (data.code === "0000") {
                currentTicket = data.data;  // 保存当前票据
                // 生成微信二维码图片URL
                elements.qrImg.src = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${currentTicket}`;
                startPolling();  // 开始轮询登录状态
            }
        } catch (error) {
            console.error('获取二维码失败:', error);
        }
    };

    // 关闭登录弹窗
    const close = () => {
        elements.mask.style.display = 'none';  // 隐藏遮罩层
        clearInterval(intervalId);            // 清除轮询定时器
        currentTicket = null;                 // 清空当前票据
    };

    // 开始轮询检查登录状态
    const startPolling = () => {
        intervalId = setInterval(async () => {
            try {
                // 查询登录状态接口
                const response = await fetch(
                    `http://127.0.0.1:8080/api/v1/login/check_login?ticket=${currentTicket}`
                );
                const data = await response.json();

                if (data.code === "0000") {
                    clearInterval(intervalId);  // 登录成功停止轮询
                    // 设置用户cookie（30天有效期）
                    document.cookie = `userId=${data.data.data}; max-age=${30 * 24 * 60 * 60}; path=/`;
                    close();                   // 关闭弹窗
                    window.location.reload();  // 刷新页面
                }
            } catch (error) {
                console.error('登录状态检查失败:', error);
            }
        }, 3000);  // 每3秒轮询一次
    };

    // 立即初始化事件监听
    initEvents();

    return { show };  // 暴露show方法
})();

// 商品模块
const ProductModule = (() => {
    let currentSlide = 0;           // 当前轮播图索引
    const carousel = document.querySelector('.carousel-inner');  // 轮播容器

    // 初始化轮播图自动切换
    const initCarousel = () => {
        setInterval(() => {
            currentSlide = (currentSlide + 1) % 3;  // 循环切换（0-1-2-0）
            carousel.style.transform = `translateX(-${currentSlide * 100}%)`;  // CSS变换实现滑动效果
        }, 3000);  // 每3秒切换一次
    };

    // 获取商品数据
    const fetchData = async () => {
        try {
            // 从Cookie获取userId，不存在时使用默认值"zwx"
            let userId = getCookie('userId');
            userId = userId !== null ? userId : "zwx";
            // 请求商品数据接口
            const response = await fetch('http://127.0.0.1:8091/api/v1/gbm/index/query_group_buy_market_config', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'User-Agent': 'Apifox/1.0.0'  // 模拟客户端标识
                },
                body: JSON.stringify({  // 请求参数
                    userId: userId,
                    source: "s01",
                    channel: "c01",
                    goodsId: "9890001"
                })
            });
            const data = await response.json();
            updateUI(data.data);  // 成功则更新界面
        } catch (error) {
            console.error('请求失败:', error);
            loadMockData();  // 失败则加载模拟数据
        }
    };

    // 加载模拟数据（降级处理）
    const loadMockData = () => {
        const mockData = {  // 模拟数据结构
            goods: {
                originalPrice: 100.0,
                deductionPrice: 50.0,
                payPrice: 50.0
            },
            teamList: [
                {
                    teamId: "T0001",
                    userId: "有效团单1",
                    targetCount: 2,
                    lockCount: 1,
                    validTimeCountdown: "00:00:10"
                }
            ],
            teamStatistic: {
                allTeamUserCount: 256
            }
        };
        updateUI(mockData);
    };

    // 更新界面数据
    const updateUI = (data) => {
        // 更新价格信息
        document.getElementById('deductionPrice').textContent = data.goods.deductionPrice;
        document.getElementById('allTeamUserCount').textContent = data.teamStatistic.allTeamUserCount;
        document.getElementById('originalPrice').textContent = data.goods.originalPrice;
        document.getElementById('payPrice').textContent = data.goods.payPrice;

        const groupList = document.getElementById('groupList');
        groupList.innerHTML = '';  // 清空现有列表

        // 过滤有效团单（验证时间格式）
        const validTeams = data.teamList?.filter(team =>
            /^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$/.test(team.validTimeCountdown)
        ) || [];

        if (validTeams.length > 0) {
            validTeams.forEach(team => createGroupItem(team, groupList));  // 创建有效团单
        } else {
            // 显示空状态提示
            groupList.innerHTML = '<div class="empty-tip">小伙伴，赶紧去开团吧，做村里最靓的仔。</div>';
        }
    };

    // 创建单个团单元素
    const createGroupItem = (team, container) => {
        const groupItem = document.createElement('div');
        groupItem.className = 'group-item';

        // 将时间转换为总秒数（HH:MM:SS -> seconds）
        let totalSeconds = team.validTimeCountdown.split(':').reduce((acc, time) => (acc * 60) + +time, 0);

        // 初始化HTML结构
        groupItem.innerHTML = `
            <div class="group-info">
                <div class="group-user">${team.userId}</div>
                <span class="team-id">${team.teamId}</span>
                <div class="group-status">
                    <span>组队仅剩${team.targetCount - team.lockCount}人，拼单即将结束</span>
                    <span class="countdown">${team.validTimeCountdown}</span>
                </div>
            </div>
            <button class="btn btn-join" data-team-id="${team.teamId}">立即参团</button>
        `;

        const countdownElement = groupItem.querySelector('.countdown');
        // 启动倒计时定时器
        const timer = setInterval(() => {
            if (--totalSeconds <= 0) {
                clearInterval(timer);  // 清除定时器
                groupItem.remove();    // 移除过期团单
                // 检查是否需要显示空状态
                if (!container.children.length) {
                    container.innerHTML = '<div class="empty-tip">小伙伴，赶紧去开团吧，做村里最靓的仔。</div>';
                }
                return;
            }

            // 重新计算时间
            const hours = Math.floor(totalSeconds / 3600);
            const minutes = Math.floor((totalSeconds % 3600) / 60);
            const seconds = totalSeconds % 60;
            // 更新倒计时显示（补零处理）
            countdownElement.textContent =
                `${String(hours).padStart(2, '0')}:` +
                `${String(minutes).padStart(2, '0')}:` +
                `${String(seconds).padStart(2, '0')}`;
        }, 1000);  // 每秒更新一次

        container.appendChild(groupItem);  // 添加团单到容器
    };

    // 处理按钮点击事件
    const handleButtonClick = async (btn) => {
        // 根据类名判断按钮类型
        const btnType = btn.classList.contains('btn-buy') ? '单独购买' :
            btn.classList.contains('btn-group') ? '发起拼团' :
                btn.classList.contains('btn-join') ? '立即参团' : null;

        if (!btnType) return;

        const teamId = btn.closest('.group-item')?.querySelector('.team-id')?.textContent;

        // 检查登录状态
        if (!document.cookie.includes('userId=')) {
            await LoginModal.show();  // 显示登录弹窗
            return false;
        }

        // 已登录执行对应操作
        switch(btnType) {
            case '单独购买':
                alert('正在进入单独购买流程...');
                break;
            case '发起拼团':
                alert('正在创建拼团订单...');
                break;
            case '立即参团':
                alert(`正在加入拼团ID: ${teamId}...`);
                break;
        }
        return true;
    };

    // 初始化事件监听（事件委托）
    const initEventListeners = () => {
        document.body.addEventListener('click', async (e) => {
            const btn = e.target.closest('.btn');  // 查找最近的按钮元素
            if (!btn) return;

            e.preventDefault();  // 阻止默认行为
            await handleButtonClick(btn);
        });
    };

    // 模块初始化
    const init = () => {
        initCarousel();  // 启动轮播图
        fetchData();     // 加载数据
        initEventListeners();  // 绑定事件
    };

    return { init };  // 暴露初始化方法
})();

// 页面初始化：DOM加载完成后启动商品模块
document.addEventListener('DOMContentLoaded', () => {
    ProductModule.init();
});

// 从Cookie中获取指定名称的值
const getCookie = (name) => {
    const value = document.cookie
        .split('; ')
        .find(row => row.startsWith(`${name}=`))
        ?.split('=')[1];
    return value ? decodeURIComponent(value) : null;
};