# !/bin/bash



inkscape --export-png=../drawable-ldpi/ic_launcher.png --export-width=36 --export-height=36 --export-background-opacity=0 --without-gui ./launcher.svg
inkscape --export-png=../drawable-mdpi/ic_launcher.png --export-width=48 --export-height=48 --export-background-opacity=0 --without-gui ./launcher.svg
inkscape --export-png=../drawable-hdpi/ic_launcher.png --export-width=72 --export-height=72 --export-background-opacity=0 --without-gui ./launcher.svg
inkscape --export-png=../drawable-xhdpi/ic_launcher.png --export-width=96 --export-height=96 --export-background-opacity=0 --without-gui ./launcher.svg
#inkscape --export-png=./PNGGenerated/android/drawable-ldpi/ic_launcher.png --export-width=512 --export-height=512 --export-background-opacity=0 --without-gui ./SVG/android/Launcher.svg

