<?php
?>
<!DOCTYPE html>
<html ng-app="app">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no,minimal-ui">
		<meta name="format-detection" content="telephone=no">
		<link rel="stylesheet" href="css/reset.css">
		<style type="text/css">
            html {
            	width: 100%;
            	height: 100%;
            	overflow-x: hidden;
            }

			body {
				text-align: left;
				width: 100%;  
				overflow: hidden;
				background: #F5EFDA; 
			}
			
			.m-read-content {
				font-size: 14px;
				color: #555;
				line-height: 31px;
				padding: 15px;
			}
			
			.m-read-content h4 {
				font-size: 20px;
				color: #736357;
				border-bottom: solid 1px #736357;
				letter-spacing: 2px;
				margin: 0 0 1em 0;
			}
			
			.m-read-content p {
				text-indent: 2em;
				margin: 0.5em 0;
				letter-spacing: 0px;
				line-height: 24px;
			}
			
			.u-tab {
				height: 34px;
				margin: 10px auto;
				line-height: 34px;
				border-radius: 8px;
				border: 1px solid #858382;
				font-size: 12px;
				background: #000;
				opacity: 0.9;
			}
			
			.u-tab li {
				display: inline-block;
				width: 49%;
				border-right: 1px solid #858382;
				text-align: center;
				color: #fff;
			}
			
			.u-tab li:nth-child(2) {
				border-right: none;
			}
			
			.m-button-bar {
				width: 70%;
				max-width: 800px;
				padding: 5px;
				margin: 0 auto;
			}
			
			.top-nav-bk {
				position: fixed;
				top: 0px;
				height: 50px;
				width: 100%;
				z-index: 9999;
				background: #000;
				opacity: 0.9;
			}
			
			.top-nav {
				position: fixed;
				top: 0px;
				height: 50px;
				width: 100%;
				z-index: 10000;
				background: none;
			}
			
			.icon-back {
				position: absolute;
				top: 14px;
				left: 10px;
				width: 23px;
				height: 23px;
				background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkJGMkEyQkQxMjdBNDExRTU4NjA2QTJDMjFDQ0I0ODhEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkJGMkEyQkQyMjdBNDExRTU4NjA2QTJDMjFDQ0I0ODhEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QkYyQTJCQ0YyN0E0MTFFNTg2MDZBMkMyMUNDQjQ4OEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QkYyQTJCRDAyN0E0MTFFNTg2MDZBMkMyMUNDQjQ4OEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4Ia560AAAHWklEQVR42uyd7W9U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);
				background-size: contain;
			}
			
			.nav-tittle {
				position: absolute;
				top: 16px;
				left: 42px;
				color: #d5d5d6;
			}
			
			.nav-pannel-bk {
				position: fixed;
				bottom:69px;
				height: 135px;
				width: 100%;
				background: #000;
				opacity: 0.9;
				z-index: 10002;
			}
			
			.nav-pannel {
				position: fixed;
				bottom:69px;
				height: 135px;
				width: 100%;
				background: none;
				color: #fff;
				z-index: 10003;
			}
			
			.child-mod {
				padding: 5px 10px;
				margin: 15px;
			}
			
			.child-mod span {
				display: inline-block;
				padding-right: 20px;
				padding-left: 10px;
			}
			
			.font-size-button {
				background: none;
				border: 1px #8c8c8c solid;
				color: #fff;
				border-radius: 16px;
				padding: 5px 40px;
			}
			
			.child-mod button:nth-child(2) {
				margin-right: 10px;
			}
			
			.bk-container-1 {
				position: relative;
				width: 30px;
				height: 30px;
				border-radius: 15px;
				background: #fff;
				display: inline-block;
				vertical-align: -14px;
			}
			
            .bk-container-current-1 {
				position: absolute;
				width: 32px;
				height: 32px;
				border-radius: 16px;
				border: 1px #ff7800 solid;
				display: inline-block;
				top: -2px;
				left: -2px;
			}
			
            .bk-container-2 {
				position: relative;
				width: 30px;
				height: 30px;
				border-radius: 15px;
				background: #F5EFDA;
				display: inline-block;
				vertical-align: -14px;
            	margin-left: 12px;
			}
			
            .bk-container-current-2 {
				position: absolute;
				width: 32px;
				height: 32px;
				border-radius: 16px;
				border: 1px #ff7800 solid;
				display: inline-block;
				top: -2px;
				left: -2px;
			}
			
            .bk-container-3 {
				position: relative;
				width: 30px;
				height: 30px;
				border-radius: 15px;
				background: #FFE4E1;
				display: inline-block;
				vertical-align: -14px;
            	margin-left: 12px;
			}
			
            .bk-container-current-3 {
				position: absolute;
				width: 32px;
				height: 32px;
				border-radius: 16px;
				border: 1px #ff7800 solid;
				display: inline-block;
				top: -2px;
				left: -2px;
			}
			
            .bk-container-4 {
				position: relative;
				width: 30px;
				height: 30px;
				border-radius: 15px;
				background: #C0EDC6;
				display: inline-block;
				vertical-align: -14px;
            	margin-left: 12px;
			}
			
            .bk-container-current-4 {
				position: absolute;
				width: 32px;
				height: 32px;
				border-radius: 16px;
				border: 1px #ff7800 solid;
				display: inline-block;
				top: -2px;
				left: -2px;
			}
			
			.bottom-nav-bk {
				position: fixed;
				bottom: 0px;
				height: 70px;
				width: 100%;
				z-index: 10004;
				opacity: 0.9;
				background: #000;
			}
			
			.bottom-nav {
				position: fixed;
				bottom: 0px;
				height: 70px;
				width: 100%;
				z-index: 10005;
				background: none;
			}
			
			.bottom-container-1 {
				float:left;
				width: 33%;
				height: 70px;	
			}
			
            .mulu-icon {
            	left: 50%;
				width: 23px;
				height: 23px;
            	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADUAAAAoCAYAAABerrI1AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkNFN0E3M0IwMjc4NDExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkNFN0E3M0IxMjc4NDExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6N0M1ODVCRkYyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6N0M1ODVDMDAyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5uTX6PAAAA0UlEQVR42uyXsQrCMBRFX4p0V39Pv8FJcekHFJe6d+v3NY6CDvUFooQ0AcEllnPhlgz3QU6bBq6ZnvdaRC7qndqoB/VR/ZC5wuxGytLtvXejUJ0uDlGgVZ8Tg6lsaWod1Jh461a9TQyMBX6hWLaSBary5zBWn8kPf8DUr/Rx8hfEPth4kxkIs+tCL4rG/VOLPH5AAQUUUEABBRRQv0O54nf1tcL6dZ3Jh9mpMH/2TkmkJFISKYmURKCAAgoooIACCihKIiWRkkhJpCR+XxJfAgwA/ROhOlYvoWQAAAAASUVORK5CYII=);
				background-size: contain;
            	margin: 12px auto 5px auto;
			}
			
			.mulu-font {
				width: 30px;
				margin: 5px auto 45px auto;
			}
			
            .bottom-container-2 {
                float:left;
				width: 33%;
				height: 70px;				
			}
			
			.font-icon {
				left: 50%;
				width: 23px;
				height: 15px;
            	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAAAuCAYAAACViW+zAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjBCRTkzQUQ3Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjBCRTkzQUQ4Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0U3QTczQkEyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MEJFOTNBRDYyNzg3MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6/hjRDAAAFqklEQVR42uRaaWwVVRi9fTbYqoBoEGIVrDsBA6JRAY0VSkQUl7pQI8YQGjTRaNCgBtdoApho3aoRRJaIC25/VBTBCiqCuKX+QDFqFANRsRCtsRWr9XzxNBnu++6bO/fNe9i+k5y0c2fmzrwz3/22mbKuv9pNChgCLgLHgBvABnCr6Qkor1CHy1ISZjVYG9leC55V6sLsB/4ucynj7T1VmEwKU09VRBHUmx6MNIS5JuF4SQhzIniKY5+MjypVYa4OtKb/PfJxvn3B7eABOY4Rp3wo2FZKzndajCiG+68otaWkLaP23rKcQoWRDHekMj5DGZPjTisVYWYqY+vB5/g3qZPuFcIMYFJn4wnrr50EDujtwlwFVlpjreBL/P9lbkdRyfMKBcmn7gRfAbeAO8G/wS7yT3Ab+AHYBF4I7ptmuJbUfzN4vDXeCN4U2X4AvNE65gtwOG80rYc6ndcdFnD+DnAe+ChCdme+wpzJyjmKLgr1VWTsOAph11A14LoURBkKrgBPTWGu98CLIE5rPktJC73NliiG5vxOgUK3WN3GlEQRnAG+Zjo7+oQKMxCsU8YXOI7XnHAd5wnFQeAb4GBl37fgXeDp4CGS09Jiy+hPxMoud0RNSSduDV1Kt4DzrbEfecHdyvHyBKSLN8galxu4L1CYp5lxRyGO9WbwcbDT00/OtYVg2VKFJdWWxGIyjtxliUMUw/HFjhwoJBqOVMoLuca54COeonT7xDmMUHbtV5d0KU0Ej7TG/gEXxpy3kMdFIfPUBghzneLMJUS/HTCXiPOYMl6bVBgtc30T/C7mPNm/KgUnvA94iTX2Cy0lFO87rNJbmCpwiqdz9XXCUzivLyqZK21g4tadSObTU96q5FSHJxFmBj18FD8wOvhgJY/fowviKDhz9XXuBccy4kg/eWkKofo3a/tAX2HkBzQo44sSODs57illvEER3Ac7mdxtTEGY/UNrpXOi5hXzQ3NBE1LmnbyXasSTafHlocJoTvJVFmRJsI3n7a12xCD6tfnM0j8CJ4UWkUeA3yjinQ2+FXBzkxS/JKH8KI/o5oP+4LHgMWA177+a20O8ZymvKDMxa9yViK1Kue0xkwlXyLmy1M/jw6pO08RcFiPp/PeOmiRt5CorcvWEbgePDrzmbpYld7gsxiXMpeALRXSGl4Evei6XpWw0JcEf7CN9Bn4Ivs4H0pVUmDXghCIK0+xxvT70UeMd+yXx+5RhfDOrbXH68u5rV47SwFsYcWBfFrDJVKP0arRmlw2tKyj4GnwYfJY5TtKaSRUm4wihtigi1LspWcc6zme3AnKFbnGs1yo/ai4bV00BogyM8+xRVBi9ab3ApNer7XI0t7Qmezemm+zm9f3gbQmddhQnJBFGKtiDrTFZa8tS9inLlAJQrnux4/gJSu10T573MD6JMFqmuyKH8wrFLkfUc7Ujhlvbn1Cc4DTF6O/GVGFGgOMczaZCQFtO4xQTzzBM21acD+rjcqBMzNNqYQ+kEJB5P/eon6RssD8jGWb0z9t8cBj4oE9aLZDPNaZ5PtU0oTWxtM9LWqxtyZTPD7ievO9abbIb9JHeQUcmKsxUxVxlDS8vsDDLFV/R32R/2LhSOfdJ4/8VhfRdZoEfmz3fomoRbXBUGG0ZPWMK/yVUGxOzuOW0mGm9nYes531eQCvqXl7lrNrr2QuSNmajZYm/gjco1x7RnfkOdZT9o1lbFBqjGWW0pC56X9czw42DtCv7xvig7VyKP5vsL9gfQvY7Syymw2S/4thUJFEM65tNisO1I4+8EZjnkWj2ixFFLHQUH4b0ordY+6+En+knwvzElDoaCmeb4mK2JUQT78vGHPZeWhLOL3M/D55k/ntptyOy724l0WyMFpE1jO3NrEyLDVk6E1lIrvU4fgxFGkv/UsWSQnxHK5dhC/3QGqe/dHy1+a8AAwCKR08FSRIHxQAAAABJRU5ErkJggg==);
				background-size: contain;
            	margin: 14px auto 8px auto;
			}
			
            .font-font {
				width: 30px;
            	margin: 8px auto 45px auto;
			}
			
            .bottom-container-3 {
                float:left;
				width: 33%;
				height: 70px;				
			}
			
            .night-icon {
            	left: 50%;
				width: 23px;
				height: 23px;
            	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkU2OUYzRUEwMjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjhBMzA2RjA2Mjc4QTExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RTY5RjNFOUUyNzg3MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RTY5RjNFOUYyNzg3MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4v5YACAAAC/UlEQVR42tSaXYhNURTH97kzRnJ9ZTQzPh7IMEr5yshHEwkRkSeSjweEiMjDePTkhcKLPCApUV54GxTKcDM0JfL9QE0UGdcdEmb7L3edOq5zzz1nn332Pvdfv5e5Z9Zd666711577etIKYUGjQILwRwwDOwDfcKAamP873CwBawDrcABP8ACU87/FWUgIk3gKMjL/7VfwV4sojxcCw6AL9JfD0DGdABhv0KTwAUwO+CZvaBfGFaYAJaDyyAb8Mwt0CksKFPh9W3gWgXnSSeFJTkBZXQrOM3VJUi9oJErUGoysBKcCuE86YYt58sF0MILtiakjTvCojI+i/o876Zh9ThNARysUCr99NpmAN5FTAvxBRgS0QZlK5+GDLQrOC9sOu/NADVm70LU+9RmYJOi8yLigk8sgPUxbEy0HUATH0RUNdV2AG0hd9xyarMdwNyYNhaDOpsBTI5pYwRYYTMAHYtwh80ARmqws1ShBdEWwGAdGyI4HrMYKAegawFSMdhlo5XIK/ZAfvoO5oFukxn4oNHeID5DjzUZwCvNNsn5DjDOVADPErA7BdwF0xP2v4YCyCVknDJwjxe2k8AHT4O0VTSeawD9Mll1glZN48T5IMc+N7p/7JJm1AFWg4ERna4Da8F1j62cdzZKY5RZBtbcEuYzuAlugyfgOSjwyS7LJ8QJYJoojuuX+RycLnqPlPXgLZfBalCB11iveyL7CM6K6tEZURxp/jNWGc2pzFbBp98M3peOVXrA4Sr49I+4zpdmgDSA94UZKXX+ITeNP8sFIDg9XWBoypyni0O6THzqN1bx6iXYCH6nyHnyZXOp8+UCIF0Fe1LiPH1FdoMr/q8G74DbwS9pT/TeO+Nes64JuFpNUl+5fdByT9wMHhl0vhu06L7opobqEPiWoON9oJ0v1bXf1LuMAcdAQbPjJ6g9juqPE+PXKvVcbjeAmQqHFsmb5iVwzu1tVKYSOkpdA1jEU246To4XxZ/gZLlUU5v8CbzhI+x9bqV74r7xHwEGAPDRVwnecW5KAAAAAElFTkSuQmCC);
				background-size: contain;
            	margin: 8px auto 5px auto;
			}
			
			.night-font {
				width: 30px;
				margin: 5px auto 45px auto;
			}
			
            .bottom-container-4 {
                float:left;
				width: 33%;
				height: 70px;				
			}
			
            .day-icon {
            	left: 50%;
				width: 23px;
				height: 23px;
            	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADkAAAA5CAYAAACMGIOFAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkNFN0E3M0I0Mjc4NDExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkNFN0E3M0I1Mjc4NDExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0U3QTczQjIyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6Q0U3QTczQjMyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6o6V5PAAADu0lEQVR42uyaTUgVURTHm/cigiS1LEitoHoqbgtcZAStngtDoYxa9rFsYYuWfUBhuSjCneiylkH2oavoEZlC4aYsM9toakWLnpsWyfQfOMJrOvdjZs6bedQc+OF7d9499/y9M/fj3HFc110Xo3WAYfp8BozG0agTs8hFsIM+L4H6f1GkvzEnjkYz6/4DS0WmIlORSlsq+bxcKSI3gkHwBlwSGA3PkrjPNE+GnhXAZYprkOLUjOmYQjT0un/aMHAMdcqNQ3GUWq+ujqknt/i+nwZDcc1vih4cojh0cQbqyQaw4P5tSfQo14OezYNGXV0b5zly5Le+mEX2KQTmTHVtl3U58BQ0lpQtgJ2Gei2gE7SDVrANVIMf4Bt4B56Dh+C9wdc80/4RMGteTNr/J/09+lhzW3WBCTeYTVA91WPwKGgPBrldS9kFBkA/qGOuN4FnbjTz6jczvuuoXa/93UHilnxmjoMVV8ZWyJ9IbFICz4FVV9ZWyW9FiDwGfmmCnQIXQBuoBxvobxuVT2nqen57khbpPYNFRYBzoNtiPnXod3MKP0VqJxGRjmaQGQXVAf1VUz3VYOQkIbJLEdADkA3pM0v1OetKQuQ4E8gHUBXxEfDqzzC+x+MW2aL4b+eFRuu8wn9LGH9hN82dTNlrMCa02xgjf347GmdmoJ0puyu8rbrHlB2MU2QrUzYuLLJg2a6VyA7KbLsMi3Tdb9sVuwRJ+2TZrjF+b+4pTd2rkk/1hky4Z+vBapkz7lzW3Ri/ze3KpTqKTNlmYYG1TNlKmJRJpiSDxpkqq/aVKdsrLHIPU/ZFkwFUxu/dYk8M3c3ZNNjnKzsEXgmKPKxo12/G+MOOri+YslPCPXnSst2yTSEjTNkBkBcSmCd/Nu1aDF/hl15cDmdGYO26SXrtGuUs5AZT1kQrn2xIn1la6TQx1/rDT0TR9pMFxUL6fogeraJ6nBWS2k+uZQZUyauPtOO38dNNv1cltZqTzvH0WOZ49oMaqlND321yPCfSbF2ad5UXufaMFiIKLER9BqOK9Ea4m2CZEk61mrOQyYDiJjUpzFpqb5nad8olkjsfvG6RC7oIRsBsSY62SN9H6Lopd3MtyvmorcCM4gD0dkxnk7eiHARHEWg84RWkUXPinZEQeSXsCa8wOYXQqxIi31aAQN3R/rTEAv1lqCNs3taSTqoEmclmqf2FIFlCm3cGvBeBzoOt4I7vraqgJvW+awPF9B0MgJ9RRZYz+5a+75qKTEWmIstuFfm+q7RJve8ayH4LMACaxEJEaXs23AAAAABJRU5ErkJggg==);
				background-size: contain;
            	margin: 8px auto 5px auto;
			}
			
			.day-font {
				width: 30px;
				margin: 5px auto 45px auto;
			}
			
			.artical-action-mid {
				position: fixed;
				z-index: 10001;
				width: 100%;
				height: 40%;
				top: 30%
			}
			
		</style>
	</head>
	<body>
	    <!-- 根div -->
		<div id="root" class="container">
		
		    <!-- 轻触唤出上下边栏的点击区域div -->
		    <div class="m-artical-action">
		      <div class="artical-action-mid" id="action_mid"></div>
		    </div>
		
		    <!-- 顶部边栏div -->
		    <div class="top-nav-bk top_nav" style="display: none"></div>
		 
		
    		<!-- 小说标题div -->
    		<div id="fiction_chapter_tittle"></div>
    		
    		<!-- 小说内容div -->
    		<div id="fiction_container" class="m-read-content">
    		  <h4>第一章 欺负</h4>
    		  <p>二十世纪九十年代，在J市第二中学教学楼的一层走廊里。</p>
    		  <p>“嘿，小子，把钱都给我拿出来！”两个头发染成花花绿绿的少年把一个身材瘦弱的学生逼在墙角。</p>
    		  <p>学生底下头，小声说：“我没有钱。”</p>
    		  <p>‘啪’两个少年中一个高个的一巴掌打在学生脸上。“草你妈的，别和我罗嗦，快点！”</p>
    		  <p>学生被打得嘴角通红，眼泪不争气的掉下来。这时高个旁边的矮胖少年说：“算了，别打坏了。这小子是我班里学习尖子，哈哈！”</p>
    		  <p>那高个看看学生：“草，看他你熊样吧。学习好有个屁用。”转头对一边的胖子说：“老肥，你去翻翻他兜，我咋不相信他没钱呢！”</p>
    		  <p>胖子‘恩’了一声，来到学生身前说：“谢文东，你把手松开。”原来那学生听见高个少年的话，用手死死抓住裤兜。</p>
    		  <p>见那个名叫谢文东的学生象没听见一样还是用手捂着兜。“草，你当我放屁是不是？”胖子一脚蹬在谢文东的小腹上。谢文东身子重重撞在墙上。胖子把他的手拉开，另支手伸进他裤兜里。拿出一张褶皱的五元钱。</p>
    		  <p>胖子把钱交给高个少年，往地下吐口吐沫：“妈的，给你脸你不要脸。”说完，和高个少年嘻嘻哈哈离开。留下满脸痛苦的谢文东。</p>
    		  <p>谢文东是J市第二中学初三学生，学习努力，头脑聪明，成绩非常优秀，在整个学校都能排在第一。但是性格有些内向，没有什么朋友，加上身材瘦小，经常受到别人欺负。第二中学在J市不是什么重点中学，学校的管理也很松懈，经常有校外年龄不大的不良少年进出。这些人年龄都不大，由于各种原因不再上学，在社会上糊混。见到软弱好欺的学生，不是找茬就是要钱，或许这样他们能体会到一种成就感吧！</p>
    		  <p>站在学校走廊里好一会，谢文东弯腰拣起掉这地上的书包，走出学校。回家的路上，谢文东眼睛里都是委屈的泪水，心里不停问自己：为什么？为什么他们总是欺负我？为什么他们不欺负别人？为什么会是我？</p>
    		</div>
    		
    		<!-- 底部翻页div -->
    		<div class="m-button-bar">
    		  <ul class="u-tab">
    		      <li id="prev_button">上一章</li>
    		      <li id="next_button">下一章</li>
    		  </ul>
    		</div>
    		
    		<!-- 底部弹出面板div -->
    		<div class="nav-pannel-bk nav_pannel" style="display: none"></div>
    		<div id="font-container" class="nav-pannel nav_pannel" style="display: none">
    		  <div class="child-mod">
    		      <span>字号</span>
    		      <button id="large-font" class="font-size-button">大</button>
    		      <button id="small-font" class="font-size-button">小</button>
    		  </div>
    		      
    		  <div class="child-mod">
    		      <span>背景</span>
    		      <div class="bk-container-1">
    		          <div class="bk-container-current-1" style="display: none"></div>
    		      </div>
    		      <div class="bk-container-2">
    		          <div class="bk-container-current-2" ></div>
    		      </div>
    		      <div class="bk-container-3">
    		          <div class="bk-container-current-3" style="display: none"></div>
    		      </div>
    		      <div class="bk-container-4">
    		          <div class="bk-container-current-4" style="display: none"></div>
    		      </div>
    		  </div>
    		</div>
    		
    		<!-- 底部边栏div -->
    		<div class="bottom-nav-bk bottom_nav" style="display: none"></div>
    		<div id="bottom-nav" class="bottom-nav bottom_nav" style="display: none">
		      <div class="bottom-container-2 bottom_container_2">
		          <div class="font-icon"></div>
		          <div class="font-font">字体</div>
		      </div>
		      <div class="bottom-container-3 bottom_container_3" >
		          <div class="night-icon night_icon"></div>
		          <div class="night-font">夜间</div>
		      </div>
		      <div class="bottom-container-4 bottom_container_4" style="display: none">
		          <div class="day-icon day_icon"></div>
		          <div class="day-font">白天</div>
		      </div>
		    </div>
		</div>
		
		<script src="lib/zepto.min.js"></script>
		<script>
			window.jQuery = $;
		</script>
		<script src="js/jquery.base64.js"></script>
		<script src="js/jquery.jsonp.js"></script>

		<script>
			(function(){
				//定义html5本地数据存储类
				var Util = (function(){
					var prefix = 'html5_reader_';
					var StorageGetter = function(key){
						return localStorage.getItem(prefix + key);
					}
					var StorageSetter = function(key,val){
						return localStorage.setItem(prefix + key,val);
					}
					return {
						StorageGetter:StorageGetter,
						StorageSetter:StorageSetter
					}
				})();

				//定义常量
				var Dom = {
						//顶部边栏
						top_nav :$('.top_nav'),
						//底部导航
						bottom_nav :$('.bottom_nav'),
						//唤起字体面板按钮
						bottom_container_2 :$('.bottom_container_2'),
						//字体面板
						nav_pannel :$('.nav_pannel'),
						//白天切换到夜间模式按钮
						bottom_container_3 :$('.bottom_container_3'),
						//夜间切换到白天模式按钮
						bottom_container_4 :$('.bottom_container_4'),
						//小说内容容器
						fiction_container :$('#fiction_container'),
				}

				//定义窗口类型常量
				var Win = $(window);

				//定义文档类型常量
				var Doc = $(document);

				//设置初始默认字号
				var initFontSize = Util.StorageGetter('font-size');
				initFontSize = parseInt(initFontSize);
				if(!initFontSize){
					initFontSize = 14;
				}
				Dom.fiction_container.css('font-size',initFontSize);

				//设置初始默认的背景颜色
				var initBackgroundColor = Util.StorageGetter('background-color');
				var initBackgroundColorCurrent = Util.StorageGetter('background-color-current');
				var initTittleFontColor = Util.StorageGetter('tittle-font-color');
				var initTittleBorderColor = Util.StorageGetter('tittle-border-color');
				var initFontColor = Util.StorageGetter('font-color');
				
				if (!initBackgroundColor) {
					initBackgroundColor = "#F5EFDA";
				}
				if (!initBackgroundColorCurrent) {
					initBackgroundColorCurrent = "bk-container-current-2";
				}
				if (!initTittleFontColor) {
					initTittleFontColor = "#736357";
				}
				if (!initTittleBorderColor) {
					initTittleBorderColor = "solid 1px #736357";
				}
				if (!initFontColor) {
					initFontColor = "#555";
				}
				
				$("body").css("background-color",initBackgroundColor);
				$('.m-read-content h4').css("color",initTittleFontColor);
				$('.m-read-content h4').css("border-bottom",initTittleBorderColor);
				$('.m-read-content').css("color",initFontColor);
				
				if (initBackgroundColorCurrent == "bk-container-current-1") {
					$('.bk-container-current-1').show();
					$('.bk-container-current-2').hide();
					$('.bk-container-current-3').hide();
					$('.bk-container-current-4').hide();
				}else if (initBackgroundColorCurrent == "bk-container-current-2") {
					$('.bk-container-current-1').hide();
					$('.bk-container-current-2').show();
					$('.bk-container-current-3').hide();
					$('.bk-container-current-4').hide();
				}else if (initBackgroundColorCurrent == "bk-container-current-3") {
					$('.bk-container-current-1').hide();
					$('.bk-container-current-2').hide();
					$('.bk-container-current-3').show();
					$('.bk-container-current-4').hide();
				}else if (initBackgroundColorCurrent == "bk-container-current-4") {
					$('.bk-container-current-1').hide();
					$('.bk-container-current-2').hide();
					$('.bk-container-current-3').hide();
					$('.bk-container-current-4').show();
				}else {
					$('.bk-container-current-1').hide();
					$('.bk-container-current-2').show();
					$('.bk-container-current-3').hide();
					$('.bk-container-current-4').hide();
				}

				//设置默认阅读模式
				var night_day_model = Util.StorageGetter("night-day-model");
				var night_day_background_color = Util.StorageGetter("night-day-background-color");
				var night_day_content_font_color = Util.StorageGetter("night-day-content-font-color");
				var night_day_tittle_font_color = Util.StorageGetter("night-day-tittle-font-color");
				var night_day_tittle_border_color = Util.StorageGetter("night-day-tittle-border-color");

				if (!night_day_model) {
					night_day_model = "day";
				}
				if (!night_day_background_color) {
					night_day_background_color = initBackgroundColor;
				}
				if (!night_day_content_font_color) {
					night_day_content_font_color = initFontColor;
				}
				if (!night_day_tittle_font_color) {
					night_day_tittle_font_color = initTittleFontColor;
				}
				if (!night_day_tittle_border_color) {
					night_day_tittle_border_color = initTittleBorderColor;
				}

				if (night_day_model == "day") {
					Dom.bottom_container_3.show();
					Dom.bottom_container_4.hide();
					$("body").css("background-color",initBackgroundColor);
					$('.m-read-content h4').css("color",initTittleFontColor);
					$('.m-read-content h4').css("border-bottom",initTittleBorderColor);
					$('.m-read-content').css("color",initFontColor);
				}else {
					Dom.bottom_container_3.hide();
					Dom.bottom_container_4.show();
					$("body").css("background-color",night_day_background_color);
					$('.m-read-content').css("color",night_day_content_font_color);
					$('.m-read-content h4').css("color",night_day_tittle_font_color);
					$('.m-read-content h4').css("border-bottom",night_day_tittle_border_color);
				}

				//入口函数
				function main() {
					//todo 整个项目的入口函数
					EventHanlder();
				}
				
				//数据层：数据交互
				function ReaderModel() {
					//todo 实现和阅读器相关的数据交互的方法
				}

				//初始化UI
				function ReaderBaseFrame() {
					//todo 渲染基本的UI结构
				}

				//控制层：
				function EventHanlder() {
					//todo 交互的事件绑定
					$('#action_mid').click(function(){	//轻触屏幕中间区域，显示顶部和底部边栏事件
						if (Dom.top_nav.css('display') == 'none') {
							Dom.top_nav.show();
							Dom.bottom_nav.show();
						}else if ((Dom.top_nav.css('display') != 'none') && (Dom.nav_pannel.css('display') != 'none')) {
							Dom.top_nav.hide();
							Dom.bottom_nav.hide();
							Dom.nav_pannel.hide();
							$('.font-icon').css("background-image","url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAAAuCAYAAACViW+zAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjBCRTkzQUQ3Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjBCRTkzQUQ4Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0U3QTczQkEyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MEJFOTNBRDYyNzg3MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6/hjRDAAAFqklEQVR42uRaaWwVVRi9fTbYqoBoEGIVrDsBA6JRAY0VSkQUl7pQI8YQGjTRaNCgBtdoApho3aoRRJaIC25/VBTBCiqCuKX+QDFqFANRsRCtsRWr9XzxNBnu++6bO/fNe9i+k5y0c2fmzrwz3/22mbKuv9pNChgCLgLHgBvABnCr6Qkor1CHy1ISZjVYG9leC55V6sLsB/4ucynj7T1VmEwKU09VRBHUmx6MNIS5JuF4SQhzIniKY5+MjypVYa4OtKb/PfJxvn3B7eABOY4Rp3wo2FZKzndajCiG+68otaWkLaP23rKcQoWRDHekMj5DGZPjTisVYWYqY+vB5/g3qZPuFcIMYFJn4wnrr50EDujtwlwFVlpjreBL/P9lbkdRyfMKBcmn7gRfAbeAO8G/wS7yT3Ab+AHYBF4I7ptmuJbUfzN4vDXeCN4U2X4AvNE65gtwOG80rYc6ndcdFnD+DnAe+ChCdme+wpzJyjmKLgr1VWTsOAph11A14LoURBkKrgBPTWGu98CLIE5rPktJC73NliiG5vxOgUK3WN3GlEQRnAG+Zjo7+oQKMxCsU8YXOI7XnHAd5wnFQeAb4GBl37fgXeDp4CGS09Jiy+hPxMoud0RNSSduDV1Kt4DzrbEfecHdyvHyBKSLN8galxu4L1CYp5lxRyGO9WbwcbDT00/OtYVg2VKFJdWWxGIyjtxliUMUw/HFjhwoJBqOVMoLuca54COeonT7xDmMUHbtV5d0KU0Ej7TG/gEXxpy3kMdFIfPUBghzneLMJUS/HTCXiPOYMl6bVBgtc30T/C7mPNm/KgUnvA94iTX2Cy0lFO87rNJbmCpwiqdz9XXCUzivLyqZK21g4tadSObTU96q5FSHJxFmBj18FD8wOvhgJY/fowviKDhz9XXuBccy4kg/eWkKofo3a/tAX2HkBzQo44sSODs57illvEER3Ac7mdxtTEGY/UNrpXOi5hXzQ3NBE1LmnbyXasSTafHlocJoTvJVFmRJsI3n7a12xCD6tfnM0j8CJ4UWkUeA3yjinQ2+FXBzkxS/JKH8KI/o5oP+4LHgMWA177+a20O8ZymvKDMxa9yViK1Kue0xkwlXyLmy1M/jw6pO08RcFiPp/PeOmiRt5CorcvWEbgePDrzmbpYld7gsxiXMpeALRXSGl4Evei6XpWw0JcEf7CN9Bn4Ivs4H0pVUmDXghCIK0+xxvT70UeMd+yXx+5RhfDOrbXH68u5rV47SwFsYcWBfFrDJVKP0arRmlw2tKyj4GnwYfJY5TtKaSRUm4wihtigi1LspWcc6zme3AnKFbnGs1yo/ai4bV00BogyM8+xRVBi9ab3ApNer7XI0t7Qmezemm+zm9f3gbQmddhQnJBFGKtiDrTFZa8tS9inLlAJQrnux4/gJSu10T573MD6JMFqmuyKH8wrFLkfUc7Ujhlvbn1Cc4DTF6O/GVGFGgOMczaZCQFtO4xQTzzBM21acD+rjcqBMzNNqYQ+kEJB5P/eon6RssD8jGWb0z9t8cBj4oE9aLZDPNaZ5PtU0oTWxtM9LWqxtyZTPD7ievO9abbIb9JHeQUcmKsxUxVxlDS8vsDDLFV/R32R/2LhSOfdJ4/8VhfRdZoEfmz3fomoRbXBUGG0ZPWMK/yVUGxOzuOW0mGm9nYes531eQCvqXl7lrNrr2QuSNmajZYm/gjco1x7RnfkOdZT9o1lbFBqjGWW0pC56X9czw42DtCv7xvig7VyKP5vsL9gfQvY7Syymw2S/4thUJFEM65tNisO1I4+8EZjnkWj2ixFFLHQUH4b0ordY+6+En+knwvzElDoaCmeb4mK2JUQT78vGHPZeWhLOL3M/D55k/ntptyOy724l0WyMFpE1jO3NrEyLDVk6E1lIrvU4fgxFGkv/UsWSQnxHK5dhC/3QGqe/dHy1+a8AAwCKR08FSRIHxQAAAABJRU5ErkJggg==')");
						}else {
							Dom.top_nav.hide();
							Dom.bottom_nav.hide();
						}
					});
					
					$('.bottom_container_2').click(function() {		//点击字体唤起字体面板
						if (Dom.nav_pannel.css('display') == 'none') {
							Dom.nav_pannel.show();
							$('.font-icon').css("background-image","url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEwAAAA0CAYAAAAg5t6HAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkNFN0E3M0I4Mjc4NDExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkNFN0E3M0I5Mjc4NDExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0U3QTczQjYyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6Q0U3QTczQjcyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7nhaYNAAANmElEQVR42uxbC1RV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')");
						}else {
							Dom.nav_pannel.hide();
							$('.font-icon').css("background-image","url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAAAuCAYAAACViW+zAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjBCRTkzQUQ3Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjBCRTkzQUQ4Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0U3QTczQkEyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MEJFOTNBRDYyNzg3MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6/hjRDAAAFqklEQVR42uRaaWwVVRi9fTbYqoBoEGIVrDsBA6JRAY0VSkQUl7pQI8YQGjTRaNCgBtdoApho3aoRRJaIC25/VBTBCiqCuKX+QDFqFANRsRCtsRWr9XzxNBnu++6bO/fNe9i+k5y0c2fmzrwz3/22mbKuv9pNChgCLgLHgBvABnCr6Qkor1CHy1ISZjVYG9leC55V6sLsB/4ucynj7T1VmEwKU09VRBHUmx6MNIS5JuF4SQhzIniKY5+MjypVYa4OtKb/PfJxvn3B7eABOY4Rp3wo2FZKzndajCiG+68otaWkLaP23rKcQoWRDHekMj5DGZPjTisVYWYqY+vB5/g3qZPuFcIMYFJn4wnrr50EDujtwlwFVlpjreBL/P9lbkdRyfMKBcmn7gRfAbeAO8G/wS7yT3Ab+AHYBF4I7ptmuJbUfzN4vDXeCN4U2X4AvNE65gtwOG80rYc6ndcdFnD+DnAe+ChCdme+wpzJyjmKLgr1VWTsOAph11A14LoURBkKrgBPTWGu98CLIE5rPktJC73NliiG5vxOgUK3WN3GlEQRnAG+Zjo7+oQKMxCsU8YXOI7XnHAd5wnFQeAb4GBl37fgXeDp4CGS09Jiy+hPxMoud0RNSSduDV1Kt4DzrbEfecHdyvHyBKSLN8galxu4L1CYp5lxRyGO9WbwcbDT00/OtYVg2VKFJdWWxGIyjtxliUMUw/HFjhwoJBqOVMoLuca54COeonT7xDmMUHbtV5d0KU0Ej7TG/gEXxpy3kMdFIfPUBghzneLMJUS/HTCXiPOYMl6bVBgtc30T/C7mPNm/KgUnvA94iTX2Cy0lFO87rNJbmCpwiqdz9XXCUzivLyqZK21g4tadSObTU96q5FSHJxFmBj18FD8wOvhgJY/fowviKDhz9XXuBccy4kg/eWkKofo3a/tAX2HkBzQo44sSODs57illvEER3Ac7mdxtTEGY/UNrpXOi5hXzQ3NBE1LmnbyXasSTafHlocJoTvJVFmRJsI3n7a12xCD6tfnM0j8CJ4UWkUeA3yjinQ2+FXBzkxS/JKH8KI/o5oP+4LHgMWA177+a20O8ZymvKDMxa9yViK1Kue0xkwlXyLmy1M/jw6pO08RcFiPp/PeOmiRt5CorcvWEbgePDrzmbpYld7gsxiXMpeALRXSGl4Evei6XpWw0JcEf7CN9Bn4Ivs4H0pVUmDXghCIK0+xxvT70UeMd+yXx+5RhfDOrbXH68u5rV47SwFsYcWBfFrDJVKP0arRmlw2tKyj4GnwYfJY5TtKaSRUm4wihtigi1LspWcc6zme3AnKFbnGs1yo/ai4bV00BogyM8+xRVBi9ab3ApNer7XI0t7Qmezemm+zm9f3gbQmddhQnJBFGKtiDrTFZa8tS9inLlAJQrnux4/gJSu10T573MD6JMFqmuyKH8wrFLkfUc7Ujhlvbn1Cc4DTF6O/GVGFGgOMczaZCQFtO4xQTzzBM21acD+rjcqBMzNNqYQ+kEJB5P/eon6RssD8jGWb0z9t8cBj4oE9aLZDPNaZ5PtU0oTWxtM9LWqxtyZTPD7ievO9abbIb9JHeQUcmKsxUxVxlDS8vsDDLFV/R32R/2LhSOfdJ4/8VhfRdZoEfmz3fomoRbXBUGG0ZPWMK/yVUGxOzuOW0mGm9nYes531eQCvqXl7lrNrr2QuSNmajZYm/gjco1x7RnfkOdZT9o1lbFBqjGWW0pC56X9czw42DtCv7xvig7VyKP5vsL9gfQvY7Syymw2S/4thUJFEM65tNisO1I4+8EZjnkWj2ixFFLHQUH4b0ordY+6+En+knwvzElDoaCmeb4mK2JUQT78vGHPZeWhLOL3M/D55k/ntptyOy724l0WyMFpE1jO3NrEyLDVk6E1lIrvU4fgxFGkv/UsWSQnxHK5dhC/3QGqe/dHy1+a8AAwCKR08FSRIHxQAAAABJRU5ErkJggg==')");
						}
					});
					
					$('.bottom_container_3').click(function() {		//由白天模式切换为夜间模式
						Dom.bottom_container_3.hide();
						Dom.bottom_container_4.show();
						
						$("body").css("background-color","#000");
						$('.m-read-content').css("color","#888");
						$('.m-read-content h4').css("color","#aaa");
						$('.m-read-content h4').css("border-bottom","solid 1px #aaa");
						
						Util.StorageSetter('night-day-model',"night");
						Util.StorageSetter('night-day-background-color',"#000");
						Util.StorageSetter('night-day-content-font-color',"#888");
						Util.StorageSetter('night-day-tittle-font-color',"#aaa");
						Util.StorageSetter('night-day-tittle-border-color',"solid 1px #aaa");
					});
					
					$('.bottom_container_4').click(function() {		//由夜间模式切换为白天模式
						Dom.bottom_container_3.show();
						Dom.bottom_container_4.hide();
						
						$("body").css("background-color",initBackgroundColor);
						$('.m-read-content').css("color","#555");
						$('.m-read-content h4').css("color","#736357");
						$('.m-read-content h4').css("border-bottom","solid 1px #736357");
						
						Util.StorageSetter('night-day-model',"day");
						Util.StorageSetter('night-day-background-color',initBackgroundColor);
						Util.StorageSetter('night-day-content-font-color',"#555");
						Util.StorageSetter('night-day-tittle-font-color',"#736357");
						Util.StorageSetter('night-day-tittle-border-color',"solid 1px #736357");
					});

					Win.scroll(function() {		//页面滚动，取消顶部和底部边栏
						Dom.top_nav.hide();
						Dom.bottom_nav.hide();
						Dom.nav_pannel.hide();
						$('.font-icon').css("background-image","url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAAAuCAYAAACViW+zAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjBCRTkzQUQ3Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjBCRTkzQUQ4Mjc4NzExRTU5RkYxQjg1Rjk2QkEyNzBEIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0U3QTczQkEyNzg0MTFFNTlGRjFCODVGOTZCQTI3MEQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MEJFOTNBRDYyNzg3MTFFNTlGRjFCODVGOTZCQTI3MEQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6/hjRDAAAFqklEQVR42uRaaWwVVRi9fTbYqoBoEGIVrDsBA6JRAY0VSkQUl7pQI8YQGjTRaNCgBtdoApho3aoRRJaIC25/VBTBCiqCuKX+QDFqFANRsRCtsRWr9XzxNBnu++6bO/fNe9i+k5y0c2fmzrwz3/22mbKuv9pNChgCLgLHgBvABnCr6Qkor1CHy1ISZjVYG9leC55V6sLsB/4ucynj7T1VmEwKU09VRBHUmx6MNIS5JuF4SQhzIniKY5+MjypVYa4OtKb/PfJxvn3B7eABOY4Rp3wo2FZKzndajCiG+68otaWkLaP23rKcQoWRDHekMj5DGZPjTisVYWYqY+vB5/g3qZPuFcIMYFJn4wnrr50EDujtwlwFVlpjreBL/P9lbkdRyfMKBcmn7gRfAbeAO8G/wS7yT3Ab+AHYBF4I7ptmuJbUfzN4vDXeCN4U2X4AvNE65gtwOG80rYc6ndcdFnD+DnAe+ChCdme+wpzJyjmKLgr1VWTsOAph11A14LoURBkKrgBPTWGu98CLIE5rPktJC73NliiG5vxOgUK3WN3GlEQRnAG+Zjo7+oQKMxCsU8YXOI7XnHAd5wnFQeAb4GBl37fgXeDp4CGS09Jiy+hPxMoud0RNSSduDV1Kt4DzrbEfecHdyvHyBKSLN8galxu4L1CYp5lxRyGO9WbwcbDT00/OtYVg2VKFJdWWxGIyjtxliUMUw/HFjhwoJBqOVMoLuca54COeonT7xDmMUHbtV5d0KU0Ej7TG/gEXxpy3kMdFIfPUBghzneLMJUS/HTCXiPOYMl6bVBgtc30T/C7mPNm/KgUnvA94iTX2Cy0lFO87rNJbmCpwiqdz9XXCUzivLyqZK21g4tadSObTU96q5FSHJxFmBj18FD8wOvhgJY/fowviKDhz9XXuBccy4kg/eWkKofo3a/tAX2HkBzQo44sSODs57illvEER3Ac7mdxtTEGY/UNrpXOi5hXzQ3NBE1LmnbyXasSTafHlocJoTvJVFmRJsI3n7a12xCD6tfnM0j8CJ4UWkUeA3yjinQ2+FXBzkxS/JKH8KI/o5oP+4LHgMWA177+a20O8ZymvKDMxa9yViK1Kue0xkwlXyLmy1M/jw6pO08RcFiPp/PeOmiRt5CorcvWEbgePDrzmbpYld7gsxiXMpeALRXSGl4Evei6XpWw0JcEf7CN9Bn4Ivs4H0pVUmDXghCIK0+xxvT70UeMd+yXx+5RhfDOrbXH68u5rV47SwFsYcWBfFrDJVKP0arRmlw2tKyj4GnwYfJY5TtKaSRUm4wihtigi1LspWcc6zme3AnKFbnGs1yo/ai4bV00BogyM8+xRVBi9ab3ApNer7XI0t7Qmezemm+zm9f3gbQmddhQnJBFGKtiDrTFZa8tS9inLlAJQrnux4/gJSu10T573MD6JMFqmuyKH8wrFLkfUc7Ujhlvbn1Cc4DTF6O/GVGFGgOMczaZCQFtO4xQTzzBM21acD+rjcqBMzNNqYQ+kEJB5P/eon6RssD8jGWb0z9t8cBj4oE9aLZDPNaZ5PtU0oTWxtM9LWqxtyZTPD7ievO9abbIb9JHeQUcmKsxUxVxlDS8vsDDLFV/R32R/2LhSOfdJ4/8VhfRdZoEfmz3fomoRbXBUGG0ZPWMK/yVUGxOzuOW0mGm9nYes531eQCvqXl7lrNrr2QuSNmajZYm/gjco1x7RnfkOdZT9o1lbFBqjGWW0pC56X9czw42DtCv7xvig7VyKP5vsL9gfQvY7Syymw2S/4thUJFEM65tNisO1I4+8EZjnkWj2ixFFLHQUH4b0ordY+6+En+knwvzElDoaCmeb4mK2JUQT78vGHPZeWhLOL3M/D55k/ntptyOy724l0WyMFpE1jO3NrEyLDVk6E1lIrvU4fgxFGkv/UsWSQnxHK5dhC/3QGqe/dHy1+a8AAwCKR08FSRIHxQAAAABJRU5ErkJggg==')");
					});

					$('#large-font').click(function() {		//点击放大字体
						if(initFontSize > 20){
							return;
						}
						initFontSize += 1;
						Dom.fiction_container.css('font-size',initFontSize);
						Util.StorageSetter('font-size',initFontSize);
					});

					$('#small-font').click(function() {		//点击缩小字体
						if(initFontSize < 12){
							return;
						}
						initFontSize -= 1;
						Dom.fiction_container.css('font-size',initFontSize);
						Util.StorageSetter('font-size',initFontSize);
					});

					$('.bk-container-1').click(function() {	//点击更换为白色背景
						$("body").css("background-color","#fff");
						$('.m-read-content').css("color","#555");
						$('.m-read-content h4').css("color","#736357");
						$('.m-read-content h4').css("border-bottom","solid 1px #736357");
						
						$('.bk-container-current-1').show();
						$('.bk-container-current-2').hide();
						$('.bk-container-current-3').hide();
						$('.bk-container-current-4').hide();
						
						Util.StorageSetter('background-color',"#fff");
						Util.StorageSetter('background-color-current',"bk-container-current-1");
						Util.StorageSetter('tittle-font-color',"#736357");
						Util.StorageSetter('tittle-border-color',"solid 1px #736357");
						Util.StorageSetter('font-color',"#555");
					});

					$('.bk-container-2').click(function() {	//点击更换为微信阅读黄色背景
						$("body").css("background-color","#F5EFDA");
						$('.m-read-content').css("color","#555");
						$('.m-read-content h4').css("color","#736357");
						$('.m-read-content h4').css("border-bottom","solid 1px #736357");
						
						$('.bk-container-current-1').hide();
						$('.bk-container-current-2').show();
						$('.bk-container-current-3').hide();
						$('.bk-container-current-4').hide();

						Util.StorageSetter('background-color',"#F5EFDA");
						Util.StorageSetter('background-color-current',"bk-container-current-2");
						Util.StorageSetter('tittle-font-color',"#736357");
						Util.StorageSetter('tittle-border-color',"solid 1px #736357");
						Util.StorageSetter('font-color',"#555");
					});

					$('.bk-container-3').click(function() {	//点击更换为粉色背景
						$("body").css("background-color","#F7EEE5");
						$('.m-read-content').css("color","#555");
						$('.m-read-content h4').css("color","#736357");
						$('.m-read-content h4').css("border-bottom","solid 1px #736357");
						
						$('.bk-container-current-1').hide();
						$('.bk-container-current-2').hide();
						$('.bk-container-current-3').show();
						$('.bk-container-current-4').hide();

						Util.StorageSetter('background-color',"#F7EEE5");
						Util.StorageSetter('background-color-current',"bk-container-current-3");
						Util.StorageSetter('tittle-font-color',"#736357");
						Util.StorageSetter('tittle-border-color',"solid 1px #736357");
						Util.StorageSetter('font-color',"#555");
					});

					$('.bk-container-4').click(function() {	//点击更换为体系结构护眼绿背景
						$("body").css("background-color","#C0EDC6");
						$('.m-read-content').css("color","#555");
						$('.m-read-content h4').css("color","#736357");
						$('.m-read-content h4').css("border-bottom","solid 1px #736357");
						
						$('.bk-container-current-1').hide();
						$('.bk-container-current-2').hide();
						$('.bk-container-current-3').hide();
						$('.bk-container-current-4').show();

						Util.StorageSetter('background-color',"#C0EDC6");
						Util.StorageSetter('background-color-current',"bk-container-current-4");
						Util.StorageSetter('tittle-font-color',"#736357");
						Util.StorageSetter('tittle-border-color',"solid 1px #736357");
						Util.StorageSetter('font-color',"#555");
					});
				}

				//调用入口函数
				main();

			})();
		</script>
	</body>
</html>
