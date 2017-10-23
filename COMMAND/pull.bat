# copy DB file
# さきに、pull_command.txtを実行
# pull.bat "自分がコピーを置きたいディレクトリ名" // (ex) /Users/5151021/Desktop
echo "pull batch start!"
adb pull /sdcard/sample.db %1