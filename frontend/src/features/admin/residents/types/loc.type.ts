export interface AdlItem {

    activity: string;

    score: number;

    description: string;

}

export interface LocResult {

    adlScore: number;

    suggestedLevelCode: string;

    suggestedLevelName: string;

    currentLevelCode: string;

    currentLevelName: string;

    dailyRate: number;

    adlItems: AdlItem[];

}

export interface CareLevelHistory {

    id: number;

    careLevelId: number;

    levelCode: string;

    startDate: string;
     action: string; 

    endDate?: string | null;

}