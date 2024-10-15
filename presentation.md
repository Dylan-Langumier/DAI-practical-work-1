---
marp: true
---
<!--
theme: gaia
size: 16:9
paginate: true
author: 'Dylan Langumier and Raphael Perret' 
title: 'BIM'
description: 'Basic Image Manipulation'
url: https://github.com/Dylan-Langumier/DAI-practical-work-1/presentation.md
footer: '**HEIG-VD** - DAI Course 2024-2025'
style: |
    :root {
        --color-background: #fff;
        --color-foreground: #333;
        --color-highlight: #f96;
        --color-dimmed: #888;
        --color-headings: #7d8ca3;
    }
    blockquote {
        font-style: italic;
    }
    table {
        width: 100%;
    }
    th:first-child {
        width: 15%;
    }
    h1, h2, h3, h4, h5, h6 {
        color: var(--color-headings);
    }
    h2, h3, h4, h5, h6 {
        font-size: 1.5rem;
    }
    h1 a:link, h2 a:link, h3 a:link, h4 a:link, h5 a:link, h6 a:link {
        text-decoration: none;
    }
    section:not([class=lead]) > p, blockquote {
        text-align: justify;
    }
headingDivider: 4
-->

[base_image]:test_images/desert.bmp
[image_200_color]:test_images/desert_200_color.bmp
[image_red]:test_images/desert_red.bmp

# Presentation of DAI project : BIM
<!--
_class: lead
_paginate: false
-->

<small>Dylan Langumier & Raphael Perret</small>

## Our project

- works with simple bmp files
- applies a variety of filters with many parameters
- simple CLI tool

## Example 1

java -jar target/DAI-filters-1.0-SNAPSHOT.jar -f COLOR_INTENSITY **test_images/desert.bmp test_images/desert_200_color.bmp** __200__ apply
![base_image]
![image_200_color]

## Example 2
1. ADJUST_COLOR 150 20 20 250  
2. MOVING_AVERAGE 2  
![base_image]
![image_red]
