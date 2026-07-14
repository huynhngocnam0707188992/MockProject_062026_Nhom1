import {
useEffect,
useState
} from "react";


import {residentLocService}
from "../services/resident-loc.service";


import type {
CareLevelHistory
}
from "../types/loc.type";


import LocSummaryCard 
from "../components/loc/loc-summary-card";


import AdlBreakdownTable
from "../components/loc/adl-breakdown-table";


import LocLevelReference
from "../components/loc/loc-level-reference";


import LocRateCard
from "../components/loc/loc-rate-card";



interface Props{

residentId:number;

}



export default function LocResultTab({
residentId
}:Props){


const [history,setHistory]
=
useState<CareLevelHistory[]>([]);



const [loading,setLoading]
=
useState(true);



useEffect(()=>{


loadData();


},[residentId]);



const loadData=async()=>{


try{


const result =
await residentLocService
.getHistory(residentId);



setHistory(result);



}
finally{

setLoading(false);

}


};



if(loading)
return <div>
Loading...
</div>



const current =
history.find(
x=>x.endDate===null
);



return (

<div className="p-8">


<h1 className="
text-2xl
font-bold
">

LOC Classification Result

</h1>


<p className="
text-gray-500
mb-8
">

Resident Care Level History

</p>



<LocSummaryCard

levelCode={
current?.levelCode ?? "N/A"
}

/>



<AdlBreakdownTable

data={history}

/>



<LocLevelReference/>


<LocRateCard/>


</div>

)


}