<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>部门管理</title>
    <jsp:include page="/common/backend_common.jsp"/>
    <jsp:include page="/common/page.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        用户管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护部门与用户关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            部门列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 dept-add"></i>
            </a>
        </div>
        <div id="deptList">
        </div>
    </div>
    <div class="col-sm-9">
        <div class="col-xs-12">
            <div class="table-header">
                用户列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 user-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length" id="dynamic-table_length"><label>
                                展示
                                <select id="pageSize" name="dynamic-table_length" aria-controls="dynamic-table" class="form-control input-sm">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select> 条记录 </label>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div id="dynamic-table_filter" class="dataTables_filter"><label>
                                搜索:
                                <input type="search" class="form-control input-sm" placeholder="" aria-controls="dynamic-table"></label></div>
                        </div>
                    </div>
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                姓名
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                所属部门
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                邮箱
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                电话
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="userList"></tbody>
                    </table>
                    <div class="row" id="userPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-dept-form" style="display: none;">
    <form id="deptForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">上级部门</label></td>
                <td>
                    <select id="parentId" name="parentId" data-placeholder="选择部门" style="width: 200px;"></select>
                    <input type="hidden" name="id" id="deptId"/>
                </td>
            </tr>
            <tr>
                <td><label for="deptName">名称</label></td>
                <td><input type="text" name="name" id="deptName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="deptSeq">顺序</label></td>
                <td><input type="text" name="seq" id="deptSeq" value="1" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="deptRemark">备注</label></td>
                <td><textarea name="remark" id="deptRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-user-form" style="display: none;">
    <form id="userForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">所在部门</label></td>
                <td>
                    <select id="deptSelectId" name="deptId" data-placeholder="选择部门" style="width: 200px;"></select>
                </td>
            </tr>
            <tr>
                <td><label for="userName">名称</label></td>
                <input type="hidden" name="id" id="userId"/>
                <td><input type="text" name="username" id="userName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userMail">邮箱</label></td>
                <td><input type="text" name="mail" id="userMail" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userTelephone">电话</label></td>
                <td><input type="text" name="telephone" id="userTelephone" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userStatus">状态</label></td>
                <td>
                    <select id="userStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                        <option value="2">删除</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="userRemark">备注</label></td>
                <td><textarea name="remark" id="userRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>

 <%--使用 mustache 模板来渲染部门列表--%>
<script id="deptListTemplate" type="x-tmpl-mustache">
<ol class="dd-list">
    {{#deptList}}
        <li class="dd-item dd2-item dept-name" id="dept_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green dept-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red dept-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/deptList}}
</ol>
</script>

<script id="userListTemplate" type="x-tmpl-mustache">
{{#userList}}
<tr role="row" class="user-name odd" data-id="{{id}}"><!--even -->
    <td><a href="#" class="user-edit" data-id="{{id}}">{{username}}</a></td>
    <td>{{showDeptName}}</td>
    <td>{{mail}}</td>
    <td>{{telephone}}</td>
    <td>{{#bold}}{{showStatus}}{{/bold}}</td> <!-- 此处套用函数对status做特殊处理 -->
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green user-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
            <a class="red user-history" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-flag bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/userList}}
</script>

<script text="application/javascript">
    $(function(){
        var deptList;
        var deptMap = {}; //用来缓存所有的部门信息
        var userMap = {}; //用来缓存所有的用户信息
        var optionStr = ""; //用于存放部门结构
        var lastClickDeptId = -1; //最后一次点击的部门

        var deptListTemplate = $("#deptListTemplate").html(); // 获取mustache模板的 html
        Mustache.parse(deptListTemplate); // 使用 mustache 来解析 html
        var userListTemplate = $('#userListTemplate').html();
        Mustache.parse(userListTemplate);

        <!--******************************部门操作****************************-->
        loadDeptTree(); //加载部门树( 通过 ajax 的方式)

        // 加载部门树
        function loadDeptTree(){
            $.ajax({
                url: "/sys/dept/tree.json",
                success: function (result) {
                    if(result.ret) {
                        deptList = result.data;
                        console.info(deptList);
                        // 使用mustache 进行渲染 (该方式只渲染第一层)
                        var rendered = Mustache.render(deptListTemplate, {deptList: result.data})

                        $("#deptList").html(rendered);
                        recursiveRenderDept(deptList);
                        bindDeptClik();
                    }else{
                        showMessage("加载部门列表", result.msg, false);
                    }
                }
            });
        }
        //递归渲染部门树
        function recursiveRenderDept(deptList) {
            if(deptList && deptList.length > 0) {
                $(deptList).each(function(i, dept) {
                    deptMap[dept.id] = dept;
                    if(dept.deptLists.length > 0) {
                        var rendered = Mustache.render(deptListTemplate, {deptList: dept.deptLists});
                        $("#dept_" + dept.id).append(rendered);
                        recursiveRenderDept(dept.deptLists);
                    }
                })
            }
        }

        //绑定部门编辑按钮, 1 点击部门名(加载用户列表), 2 点击编辑按钮, 3 点击删除按钮
        function bindDeptClik() {
            //1 点击部门名(加载用户列表)
            $(".dept-name").click(function(e) {
                e.preventDefault(); //取消默认操作
                e.stopPropagation(); //组织冒泡时间
                var deptId = $(this).attr("data-id");
                //执行选中操作
                handleDeptSelected(deptId);
            });
            // 2 点击编辑按钮
            $(".dept-edit").click(function(e) {
                e.preventDefault(); //取消默认操作
                e.stopPropagation(); //组织冒泡时间
                var deptId = $(this).attr("data-id");

                // 编辑  该部门
                $("#dialog-dept-form").dialog({
                    model: true,
                    title: "编辑部门",
                    open: function (event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        optionStr = "<option value='0'>--请选择部门--</option>"
                        recursiveRenderDeptSelect(deptList, 1);
                        $("#deptForm")[0].reset();
                        $("#parentId").html(optionStr);
                        $("#deptId").val(deptId);
                        // 编辑部门时, 需要将该部门的信息展示出来
                        var targetDept = deptMap[deptId]; // 部门id (隐藏字段)
                        if(targetDept) {
                            $("#parentId").val(targetDept.parentId);// parentId
                            $("#deptName").val(targetDept.name)//name
                            $("#deptSeq").val(targetDept.seq)//seq
                            $("#deptRemark").val(targetDept.remark)//remark
                        }

                    },
                    buttons: {
                        "更新": function (e) {
                            e.preventDefault(); // 取消默认操作
                            updateDept(false, function successCallback(data) {
                                $("#dialog-dept-form").dialog("close");
                            }, function failCallback(data) {
                                showMessage("更新部门失败", data.msg, false);
                            });
                        },
                        "取消": function () {
                            $("#dialog-dept-form").dialog("close");
                        }
                    }
                });

            })
            // 3 点击删除按钮
            $(".dept-delete").click(function (e) {
                var deptId = $(this).attr("data-id");
                var deptName = $(this).attr("data-name");
                if (confirm("确定要删除部门: "+deptName + "么?")) {
                    //TODO:
                    console.log("删除部门: " + deptName);
                }
            });
        }

        // 新增部门操作
        $(".dept-add").click(function() {
            $("#dialog-dept-form").dialog({
                model: true,
                title: "新增部门",
                open: function (event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    optionStr = "<option value='0'>--请选择部门--</option>"
                    recursiveRenderDeptSelect(deptList, 1);
                    $("#deptForm")[0].reset();
                    $("#parentId").html(optionStr);
                },
                buttons: {
                    "添加": function (e) {
                        e.preventDefault(); // 取消默认操作
                        updateDept(true, function successCallback(data) {
                            $("#dialog-dept-form").dialog("close");
                        }, function failCallback(data) {
                            showMessage("新增部门失败", data.msg, false);
                        });
                    },
                    "取消": function () {
                        $("#dialog-dept-form").dialog("close");
                    }
                }
            });
        })

        //处理部门选中操作
        function handleDeptSelected(deptId) {
            //移除上一次被选中的部门的高亮样式
            if(lastClickDeptId != -1) {
                var lastDept = $("#dept_" + lastClickDeptId + " .dd2-content:first"); //取出上一次选中的部门标签
                lastDept.removeClass("btn-yellow"); //移除样式
                lastDept.removeClass("no-hover");
            }
            //为当前被选中的部门添加高亮的样式
            var currentDept = $("#dept_" + deptId + " .dd2-content:first");
            currentDept.addClass("btn-yellow");
            currentDept.addClass("no-hover");
            lastClickDeptId = deptId;

            //处理完样式, 需要加载该部门下的用户列表
            loadUserList(deptId);
        }

        //递归添加部门信息到  optionStr,用于新增部门时选择的部门树
        function recursiveRenderDeptSelect(deptList, level) {
            levle = level | 0; //levle 如果没有传值, 就为0
            if(deptList && deptList.length > 0) {
                $(deptList).each(function(i, dept) {
                    deptMap[dept.id] = dept;
                    var blank = "";
                    if(level > 1) {
                        for (var i = 3; i <= level; i++) {
                            blank += "..";
                        }
                        blank += "∟";
                    }
                    optionStr += Mustache.render("<option value='{{id}}'>{{name}}</option>", {id: dept.id, name: blank + dept.name})
                    if(dept.deptLists && dept.deptLists.length > 0) {
                        recursiveRenderDeptSelect(dept.deptLists, level + 1);
                    }
                })
            }
        }

        //用于添加获取更新部门的函数
        function updateDept(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? '/sys/dept/save.json' : '/sys/dept/update.json',
                data: $("#deptForm").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if(result.ret) {
                        //添加成功后, 重新加载部门树, 调用成功回掉函数
                        loadDeptTree();
                        if(successCallback) successCallback(result);
                    }else {
                        //添加失败, 调用失败回掉函数
                        if(failCallback) failCallback(result);
                    }
                }
            });
        }

        // 加载对应部门下的用户信息: 部门点击事件
        function loadUserList(deptId) {
            //TODO:
            var pageSize = $("#pageSize").val();
            var url = "/sys/User/page.json?deptId=" + deptId;
            var pageNo = $("#userPage .pageNo").val() || 1;
            $.ajax({
                url: url,
                type:'post',
                data: {
                    pageSize: pageSize,
                    pageNo: pageNo
                },
                success: function (result) {
                    renderUserListAndPage(result, url);
                }
            });
        }

        <!--******************************用户操作****************************-->

        //渲染用户列表
        function  renderUserListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0) {
                    var rendered = Mustache.render(userListTemplate, {
                        "userList": result.data.data,
                        "showDeptName": function () {
                            return deptMap[this.deptId].name;
                        },
                        "showStatus": function () {
                            return this.status == 1 ? "有效" : (this.status == 0 ? "无效" : "删除");
                        },
                        "bold": function () { // 对展示做特殊处理
                            return function (text, render) {
                                var status = render(text); // 获取出渲染后的值
                                if (status == '有效') {
                                    return "<span class='label label-sm label-success'>有效</span>";
                                } else if (status == '无效') {
                                    return "<span class='label label-sm label-warning'>无效</span>";
                                } else {
                                    return "<span class='label'>删除</span>";
                                }
                            }
                        }
                    });
                    $('#userList').html(rendered);
                    bindUserClick();
                    $.each(result.data.data, function (i, user) {
                        userMap[user.id] = user;
                    });
                } else {
                    $('#userList').html('');
                }
                var pageSize = $("#pageSize").val();
                var pageNo = $("#userPage .pageNo").val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "userPage", renderUserListAndPage);
            } else {
                showMessage("获取部门下用户列表", result.msg, false);
            }
        }

        // 绑定用户相关点击事件
        function bindUserClick() {
            // 处理点击[编辑用户]按钮
            $(".user-edit").click(function (e) {
                e.preventDefault();
                e.stopPropagation(); // 此处必须要取消冒泡,因为是个递归结构,冒泡的话会让一个点击被响应多个
                var userId = $(this).attr("data-id"); // 选中的部门id
                $("#dialog-user-form").dialog({
                    modal: true,
                    title: "编辑用户",
                    open: function (event, ui) {
                        $("#userForm")[0].reset();
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide(); // 点开时隐藏关闭按钮
                        optionStr = "";
                        recursiveRenderDeptSelect(deptList, 1);
                        $("#deptSelectId").html(optionStr);
                        var targetUser = userMap[userId];
                        if (targetUser) {
                            $("#deptSelectId").val(targetUser.deptId);
                            $("#userName").val(targetUser.username);
                            $("#userMail").val(targetUser.mail);
                            $("#userTelephone").val(targetUser.telephone);
                            $("#userStatus").val(targetUser.status);
                            $("#userRemark").val(targetUser.remark);
                            $("#userId").val(targetUser.id);
                        }
                    },
                    buttons: {
                        "更新": function (e) {
                            e.preventDefault();
                            updateUser(false, function (data) { // success callback
                                userMap[userId].deptId = $("#deptSelectId").val();
                                userMap[userId].username = $("#userName").val();
                                userMap[userId].mail = $("#userMail").val();
                                userMap[userId].telephone = $("#userTelephone").val();
                                userMap[userId].status = $("#userStatus").val();
                                userMap[userId].remark = $("#userRemark").val();
                                $("#dialog-user-form").dialog("close");
                                showMessage("更新用户", "请刷新列表查看最新数据", true);
                            }, function (data) { // fail callback
                                showMessage("更新用户", data.msg, false);
                            });
                        },
                        "取消": function () {
                            $("#dialog-user-form").dialog("close");
                        }
                    }
                });
            });
        }
        
        //新增用户操作
        $(".user-add").click(function () {
            $("#dialog-user-form").dialog({
                model: true,
                title: "新增用户", open: function (event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    optionStr = "<option value=''>--请选择部门--</option>"
                    recursiveRenderDeptSelect(deptList, 1);
                    $("#deptForm")[0].reset();
                    $("#deptSelectId").html(optionStr);
                },
                buttons: {
                    "添加": function (e) {
                        e.preventDefault(); // 取消默认操作
                        updateUser(true, function successCallback(data) {
                            $("#dialog-user-form").dialog("close");
                            loadUserList(lastClickDeptId);
                        }, function failCallback(data) {
                            showMessage("新增用户失败", data.msg, false);
                        });
                    },
                    "取消": function () {
                        $("#dialog-user-form").dialog("close");
                    }
                }
            });
        })

        //绑定用户编辑按钮, 1 点击部门名(加载用户列表), 2 点击编辑按钮, 3 点击删除按钮
        function bindUserClik() {
            //1 点击部门名(加载用户列表)
            $(".user-name").click(function(e) {
                e.preventDefault(); //取消默认操作
                e.stopPropagation(); //组织冒泡时间
                var deptId = $(this).attr("data-id");
                //执行选中操作
                handleDeptSelected(deptId);
            });
            // 2 点击编辑按钮
            $(".user-edit").click(function(e) {
                e.preventDefault(); //取消默认操作
                e.stopPropagation(); //组织冒泡时间
                var userId = $(this).attr("data-id");

                // 编辑  该部门
                $("#dialog-user-form").dialog({
                    model: true,
                    title: "编辑用户",
                    open: function (event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        optionStr = "<option value='0'>--请选择部门--</option>"
                        recursiveRenderDeptSelect(deptList, 1);
                        $("#userForm")[0].reset();
                        $("#deptSelectId").html(optionStr);
                        // 编辑用户时, 需要将该部门的信息展示出来
                        var targetUser = userMap[userId]; // 部门id (隐藏字段)
                        if(targetUser) {
                            $("#deptSelectId").val(targetUser.deptId);// parentId
                            $("#userName").val(targetUser.username)//name
                            $("#userMail").val(targetUser.mail)//name
                            $("#usertelephone").val(targetUser.telephone)//name
                            $("#userStatus").val(targetUser.status)//name
                            $("#userRemark").val(targetUser.remark)//name
                            $("#userId").val(targetUser.id)//name
                        }

                    },
                    buttons: {
                        "更新": function (e) {
                            e.preventDefault(); // 取消默认操作
                            updateUser(false, function successCallback(data) {
                                $("#dialog-user-form").dialog("close");
                                loadUserList(lastClickDeptId);
                            }, function failCallback(data) {
                                showMessage("更新用户失败", data.msg, false);
                            });
                        },
                        "取消": function () {
                            $("#dialog-user-form").dialog("close");
                        }
                    }
                });

            })
            // 3 点击删除按钮
            $(".dept-delete").click(function (e) {
                var deptId = $(this).attr("data-id");
                var deptName = $(this).attr("data-name");
                if (confirm("确定要删除部门: "+deptName + "么?")) {
                    //TODO:
                    console.log("删除部门: " + deptName);
                }
            });
        }
        //编辑用户

        //新增用户
        function updateUser(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? '/sys/User/save.json' : '/sys/User/update.json',
                data: $("#userForm").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if(result.ret) {
                        //添加成功后, 重新加载部门树, 调用成功回掉函数
                        loadDeptTree();
                        if(successCallback) successCallback(result);
                    }else {
                        //添加失败, 调用失败回掉函数
                        if(failCallback) failCallback(result);
                    }
                }
            });
        }


    })
</script>
</body>
</html>